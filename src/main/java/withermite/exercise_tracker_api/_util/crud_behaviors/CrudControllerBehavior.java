package withermite.exercise_tracker_api._util.crud_behaviors;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import withermite.exercise_tracker_api._util.ResourceWrapper;

public class CrudControllerBehavior<E extends Entity, S extends CrudService<E>> {
    private final S service;
    private final int defaultPageSize;
    private final String resourceUri;

    public CrudControllerBehavior(S service, String resourceUri, int defaultPageSize) {
        this.service = service;
        this.resourceUri = resourceUri;
        this.defaultPageSize = defaultPageSize;
    }

    public ResponseEntity<List<E>> getMany(PaginationParams params) {
        int limit = params.getLimitOrDefault(defaultPageSize);
        int offset = params.getOffsetOrDefault(0);

        List<E> entities = service.findMany(limit, offset);
        return ResponseEntity.ok().body(entities);

    }

    public ResponseEntity<E> create(E entity) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{key}").buildAndExpand(entity.fetchUriKeys()).toUri();

        return ResponseEntity.created(location).body(service.create(entity));
    }

    public ResponseEntity<E> getOne(String key) {
        E entity = service.findOne(key);

        return ResponseEntity.ok().body(entity);
    }

    public ResponseEntity<E> replace(String key, E entity) {
        ResourceWrapper<E> replaced = service.replace(key, entity);
        E replacedEntity = replaced.resource;
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(resourceUri + "/{key}").buildAndExpand(replacedEntity.fetchUriKeys()).toUri();

        if (replaced.wasCreated) {
            return ResponseEntity.created(location).body(replacedEntity);
        }

        if (!key.equals(replacedEntity.fetchUriKeys().get("key"))) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .header("Location", location.toString())
                    .body(replacedEntity);
        }

        return ResponseEntity.ok().body(replacedEntity);
    }

    public ResponseEntity<E> update(String key, E entity) {
        E newEntity = service.update(key, entity);

        if (!key.equals(newEntity.fetchUriKeys().get("key"))) {
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(resourceUri + "/{key}").buildAndExpand(newEntity.fetchUriKeys()).toUri();

            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .header("Location", location.toString())
                    .body(newEntity);
        }

        return ResponseEntity.ok().body(newEntity);
    }

    public ResponseEntity<Void> delete(String key) {
        service.delete(key);
        return ResponseEntity.noContent().build();
    }
}
