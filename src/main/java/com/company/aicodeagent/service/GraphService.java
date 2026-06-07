package com.company.aicodeagent.service;

import com.company.aicodeagent.dto.*;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {

    public GraphResponse
    buildGraph() {

        GraphResponse response =
                new GraphResponse();

        response.setNodes(
                List.of(
                        new GraphNode(
                                "customer-service"),

                        new GraphNode(
                                "CustomerClient"),

                        new GraphNode(
                                "BillingService")
                ));

        response.setEdges(
                List.of(
                        new GraphEdge(
                                "customer-service",
                                "CustomerClient"),

                        new GraphEdge(
                                "customer-service",
                                "BillingService")
                ));

        return response;
    }
}