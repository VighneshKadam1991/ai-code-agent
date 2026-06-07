import PromptPanel
    from "../components/PromptPanel";

import ServiceGraph
    from "../components/ServiceGraph";

import ImpactPanel
    from "../components/ImpactPanel";

import PatchPanel
    from "../components/PatchPanel";

function Dashboard() {

    return (

        <div>

            <PromptPanel />

            <ServiceGraph />

            <ImpactPanel />

            <PatchPanel />

        </div>

    );
}

export default Dashboard;