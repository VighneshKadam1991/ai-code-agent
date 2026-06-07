import Dashboard
    from "./pages/Dashboard";

import {
    AnalysisProvider
}
from "./context/AnalysisContext";

function App() {

    return (

        <AnalysisProvider>

            <Dashboard />

        </AnalysisProvider>
    );
}

export default App;