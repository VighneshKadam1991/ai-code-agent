import { useEffect, useState }
    from "react";

import ReactFlow
from "reactflow";

import "reactflow/dist/style.css";

import api
from "../services/api";

function ServiceGraph() {

    const [
        nodes,
        setNodes
    ] = useState([]);

    const [
        edges,
        setEdges
    ] = useState([]);

    useEffect(() => {

        loadGraph();

    }, []);

    const loadGraph =
        async () => {

            const response =
                await api.get(
                    "/graph/services");

            const graphNodes =
                response.data.nodes
                    .map(
                        (
                            node,
                            index
                        ) => ({

                            id: node.id,

                            data: {
                                label:
                                    node.id
                            },

                            position: {
                                x: 250,
                                y: index * 150
                            }
                        }));

            const graphEdges =
                response.data.edges
                    .map(
                        edge => ({

                            id:
                                edge.source
                                + "-"
                                + edge.target,

                            source:
                                edge.source,

                            target:
                                edge.target
                        }));

            setNodes(
                graphNodes);

            setEdges(
                graphEdges);
        };

    return (

        <div
            style={{
                width: "1000px",
                height: "400px",
                margin: "30px auto",
                border:
                    "1px solid #ddd"
            }}>

            <h2>
                Service Graph
            </h2>

            <ReactFlow
                nodes={nodes}
                edges={edges}
                fitView
            />

        </div>
    );
}

export default ServiceGraph;