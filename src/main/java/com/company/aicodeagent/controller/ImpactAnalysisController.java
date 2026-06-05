package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.*;
import com.company.aicodeagent.entity.ImpactResultEntity;
import com.company.aicodeagent.entity.ServiceDependencyEntity;
import com.company.aicodeagent.service.ImpactAnalysisService;
import com.company.aicodeagent.service.ServiceMethodImpact;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/impact")
public class ImpactAnalysisController {

    private final ImpactAnalysisService
            impactAnalysisService;

    public ImpactAnalysisController(
            ImpactAnalysisService impactAnalysisService) {

        this.impactAnalysisService =
                impactAnalysisService;
    }

    @PostMapping("/field-rename")
    public List<ImpactResultEntity>
    analyzeFieldRename(
            @RequestBody
            FieldRenameRequest request) {

        return impactAnalysisService
                .analyzeFieldRename(
                        request.getEntity(),
                        request.getOldField());
    }

    @PostMapping("/method-rename")
    public List<ImpactResultEntity>
    analyzeMethodRename(
            @RequestBody
            MethodRenameRequest request) {

        return impactAnalysisService
                .analyzeMethodRename(
                        request.getEntity(),
                        request.getOldMethod());
    }

    @PostMapping(
            "/field-change")
    public List<CodeChangeImpact>
    fieldChange(
            @RequestBody
            FieldChangeRequest request) {

        return impactAnalysisService
                .fieldChangeImpact(
                        request);
    }

    @PostMapping("/entity-change")
    public List<CodeChangeImpact>
    entityChange(
            @RequestBody
            EntityChangeRequest request) {

        return impactAnalysisService
                .entityChangeImpact(
                        request);
    }

    @PostMapping("/entity-change/required")
    public List<CodeChangeImpact>
    entityChangeRequired(
            @RequestBody
            EntityChangeRequest request) {

        return impactAnalysisService
                .entityChangeImpactRequired(
                        request);
    }

    @PostMapping("/service-change")
    public List<ServiceDependencyEntity>
    serviceChange(
            @RequestBody
            ServiceChangeRequest request) {

        return impactAnalysisService
                .serviceImpact(
                        request.getService());
    }

    @PostMapping(
            "/service-change/files")
    public List<CodeChangeImpact>
    serviceChangeFiles(
            @RequestBody
            ServiceChangeRequest request) {

        return impactAnalysisService
                .serviceImpactFiles(
                        request.getService());
    }

    @PostMapping(
            "/service-change/methods")
    public List<ServiceMethodImpact>
    serviceChangeMethods(
            @RequestBody
            ServiceChangeRequest request) {

        return impactAnalysisService
                .serviceImpactMethods(
                        request.getService());
    }

    @PostMapping(
            "/service-api-change")
    public List<CodeChangeImpact>
    serviceApiChange(
            @RequestBody
            ServiceApiChangeRequest request) {

        return impactAnalysisService
                .serviceApiChangeImpact(
                        request);
    }

    @PostMapping(
            "/service-field-change")
    public List<CodeChangeImpact>
    serviceFieldChange(
            @RequestBody
            ServiceFieldChangeRequest request) {

        return impactAnalysisService
                .serviceFieldChangeImpact(
                        request);
    }

    @PostMapping(
            "/service-method-change")
    public List<CodeChangeImpact>
    methodRename(
            @RequestBody
            MethodChangeRequest request) {

        return impactAnalysisService
                .serviceMethodChangeImpact(
                        request);
    }
}