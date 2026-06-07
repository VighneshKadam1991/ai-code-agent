import {
    useContext
}
from "react";

import {
    AnalysisContext
}
from "../context/AnalysisContext";

function ImpactPanel() {

    const {
        analysis
    } =
    useContext(
        AnalysisContext);

    if (!analysis) {

        return (

            <div
                style={{
                    width: "1000px",
                    margin: "30px auto",
                    textAlign: "left",
                    padding: "20px",
                    border: "1px solid #ddd",
                    borderRadius: "10px",
                    backgroundColor: "#fff"
                }}>

                <h2>
                    Impact Panel
                </h2>

                <p>
                    No analysis yet.
                </p>

            </div>
        );
    }

    return (

        <div
            style={{
                width: "1000px",
                margin: "30px auto",
                textAlign: "left",
                padding: "20px",
                border: "1px solid #ddd",
                borderRadius: "10px",
                backgroundColor: "#fff"
            }}>

            <h2>
                Impact Panel
            </h2>

            <h3>
                Summary
            </h3>

            <p
                style={{
                    lineHeight: "1.8"
                }}>
                {analysis.summary}
            </p>

            <h3>
                Risk
            </h3>

            <p
                style={{
                    lineHeight: "1.8"
                }}>
                {analysis.risk}
            </p>

            <h3>
                Migration Plan
            </h3>

            <p
                style={{
                    lineHeight: "1.8"
                }}>
                {analysis.migrationPlan}
            </p>

            <h3>
                Deployment Order
            </h3>

            <p
                style={{
                    lineHeight: "1.8"
                }}>
                {analysis.deploymentOrder}
            </p>

            <h3>
                Impacted Files
            </h3>

            <p>
                Total Files: {analysis.impacts?.length || 0}
            </p>

            <p>
                Repositories Impacted: {
                    [...new Set(
                        analysis.impacts?.map(
                            impact => impact.repo
                        ) || []
                    )].join(", ")
                }
            </p>

            <table
                border="1"
                style={{
                    width: "100%",
                    tableLayout: "fixed",
                    marginTop: "20px"
                }}>

                <thead>

                <tr>

                    <th
                        style={{
                            width: "15%"
                        }}>
                        Repository
                    </th>

                    <th
                        style={{
                            width: "30%"
                        }}>
                        File
                    </th>

                    <th>
                        Old Code
                    </th>

                    <th>
                        New Code
                    </th>

                </tr>

                </thead>

                <tbody>

                {
                    analysis.impacts?.map(
                        (
                            impact,
                            index
                        ) => (

                            <tr
                                key={index}>

                                <td>
                                    {impact.repo}
                                </td>

                                <td>
                                    {
                                        impact.file
                                            ?.split("\\")
                                            ?.pop()
                                    }
                                </td>

                                <td>

                                    <pre
                                        style={{
                                            whiteSpace: "pre-wrap",
                                            margin: 0
                                        }}>

                                        {impact.oldCode}

                                    </pre>

                                </td>

                                <td>

                                    <pre
                                        style={{
                                            whiteSpace: "pre-wrap",
                                            margin: 0
                                        }}>

                                        {impact.newCode}

                                    </pre>

                                </td>

                            </tr>
                        ))
                }

                </tbody>

            </table>

        </div>
    );
}

export default ImpactPanel;