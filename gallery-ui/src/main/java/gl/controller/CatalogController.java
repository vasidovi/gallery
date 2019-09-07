package gl.controller;

import gl.model.entity.CatalogEntity;
import gl.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CatalogController {

    @Autowired
    private CatalogService service;

    @GetMapping("/catalogs")
    public List<CatalogEntity> getAllCatalogs() {
        return service.findAll();
    }

    @GetMapping("/catalog/{id}")
    public CatalogEntity getById(
            @PathVariable Long id
    ) {
        return service.findById(id);
    }





}
