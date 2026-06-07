import {
    useState
} from "react";

import api
    from "../services/api";

function PatchPanel() {

    const [
        prompt,
        setPrompt
    ] = useState("");

    const [
        patches,
        setPatches
    ] = useState([]);

    const generatePatch =
        async () => {

            try {

                const response =
                    await api.post(
                        "/analysis/ai/generate-full-patch",
                        {
                            prompt
                        });

                setPatches(
                    response.data);

            } catch (ex) {

                console.error(
                    ex);
            }
        };

    return (

        <div
            style={{
                width: "1000px",
                margin: "30px auto",
                textAlign: "left",
                padding: "20px",
                border: "1px solid #ddd",
                borderRadius: "10px"
            }}>

            <h2>
                Patch Panel
            </h2>

            <input
                value={prompt}
                onChange={
                    e =>
                        setPrompt(
                            e.target.value)
                }
                placeholder=
                    "Enter same change request"
                style={{
                    width: "600px"
                }}
            />

            <button
                onClick={
                    generatePatch
                }>
                Generate Patch
            </button>

            {
                patches.map(
                    (
                        patch,
                        index
                    ) => (

                        <div
                            key={index}
                            style={{
                                marginTop: "30px"
                            }}>

                            <h3>
                                {patch.file
                                    ?.split("\\")
                                    ?.pop()}
                            </h3>

                            <h4>
                                Original
                            </h4>

                            <pre
                                style={{
                                    background: "#f4f4f4",
                                    padding: "10px",
                                    overflowX: "auto"
                                }}>

                                {patch.originalFile}

                            </pre>

                            <h4>
                                Updated
                            </h4>

                            <pre
                                style={{
                                    background: "#f4f4f4",
                                    padding: "10px",
                                    overflowX: "auto"
                                }}>

                                {patch.updatedFile}

                            </pre>

                        </div>
                    ))
            }

        </div>
    );
}

export default PatchPanel;