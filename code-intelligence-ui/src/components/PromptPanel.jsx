import {
    useContext,
    useState
}
from "react";

import api
    from "../services/api";

import {
    AnalysisContext
}
from "../context/AnalysisContext";

function PromptPanel() {

    const [prompt,
        setPrompt] =
            useState("");

    const {
        setAnalysis
    }
    =
    useContext(
        AnalysisContext);

    const analyze =
        async () => {

            try {

                const response =
                    await api.post(
                        "/analysis/ai/chat",
                        {
                            prompt
                        });

                setAnalysis(
                    response.data);

            } catch (ex) {

                console.error(
                    ex);
            }
        };

    return (

        <div>

            <h2>
                AI Change Analysis
            </h2>

            <input
                value={prompt}
                onChange={
                    e =>
                        setPrompt(
                            e.target.value)
                }
                placeholder=
                    "Enter change request"
                style={{
                    width: "600px"
                }}
            />

            <button
                onClick={
                    analyze
                }>
                Analyze
            </button>

        </div>
    );
}

export default PromptPanel;