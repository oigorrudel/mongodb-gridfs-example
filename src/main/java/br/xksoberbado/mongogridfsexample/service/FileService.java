package br.xksoberbado.mongogridfsexample.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.gridfs.GridFsCriteria.whereFilename;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final GridFsTemplate template;

    public GridFSFile getByName(final String name) {
        log.info("Getting by name. [file: {}]", name);

        return template.findOne(query(whereFilename().is(name)));
    }

    public Resource download(final String name) {
        log.info("Downloading. [file: {}]", name);

        return template.getResource(
            getByName(name)
        );
    }

    @SneakyThrows
    public void create(final MultipartFile file) {
        final var originalFilename = file.getOriginalFilename();
        final var contentType = MediaTypeFactory.getMediaType(originalFilename).get().toString();

        log.info("Creating. [file: {}]", originalFilename);

        template.store(file.getInputStream(), originalFilename, contentType);
    }

    public void delete(final String name) {
        log.info("Deleting. [file: {}]", name);

        template.delete(query(whereFilename().is(name)));
    }
}
