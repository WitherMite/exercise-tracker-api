package withermite.exercise_tracker_api._util.crud_behaviors;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import withermite.exercise_tracker_api._util.ResourceWrapper;

public class CrudControllerBehavior<E extends Entity<?>, S extends CrudService<E>> {
    private final S service;
    private final int defaultPageSize;
    private final String resourceUri;

    public CrudControllerBehavior(S service, String resourceUri, int defaultPageSize) {
        this.service = service;
        this.resourceUri = resourceUri;
        this.defaultPageSize = defaultPageSize;
    }

    public ResponseEntity<List<E>> getMany(Map<String, String> params) {
        try {
            int limit = params.containsKey("limit")
                    ? Integer.parseInt(params.get("limit"))
                    : defaultPageSize;
            int offset = params.containsKey("offset")
                    ? Integer.parseInt(params.get("offset"))
                    : 0;

            List<E> entities = service.findMany(limit, offset);
            return ResponseEntity.ok().body(entities);

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<E> create(E entity) {
        ResourceWrapper<E> created = service.create(entity);

        if (created == null) {
            return ResponseEntity.badRequest().build();
        }

        if (created.problems != null) {
            return ResponseEntity.status(409).build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{key}").buildAndExpand(entity.fetchKeyValue()).toUri();

        return ResponseEntity.created(location).body(created.resource);
    }

    public ResponseEntity<E> getOne(String key) {
        E entity = service.findOne(key);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(entity);
    }

    public ResponseEntity<E> replace(String key, E entity) {
        ResourceWrapper<E> replaced = service.replace(key, entity);
        E replacedEntity = replaced.resource;

        if (replacedEntity == null) {
            return ResponseEntity.badRequest().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(resourceUri + "/{key}").buildAndExpand(replacedEntity.fetchKeyValue()).toUri();

        if (replaced.wasCreated) {
            return ResponseEntity.created(location).body(replacedEntity);
        }

        if (!key.equals(replacedEntity.fetchKeyValue())) {
            return ResponseEntity.status(303)
                    .header("Location", location.toString())
                    .body(replacedEntity);
        }

        return ResponseEntity.ok().body(replacedEntity);
    }

    public ResponseEntity<E> update(String key, E entity) {
        E newEntity = service.update(key, entity);

        if (newEntity == null) {
            return ResponseEntity.notFound().build();
        }

        if (!key.equals(newEntity.fetchKeyValue())) {
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(resourceUri + "/{key}").buildAndExpand(newEntity.fetchKeyValue()).toUri();

            return ResponseEntity.status(303)
                    .header("Location", location.toString())
                    .body(newEntity);
        }

        return ResponseEntity.ok().body(newEntity);
    }

    public ResponseEntity<Void> delete(String key) {
        boolean deleted = service.delete(key);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
