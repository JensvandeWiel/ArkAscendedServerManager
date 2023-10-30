import React from 'react'
import ReactDOM, {createRoot} from 'react-dom/client'
import App from './App'
import './index.css'
import '@fontsource-variable/inter';
import {
    Button,
    CssBaseline,
    CssVarsProvider,
    getInitColorSchemeScript,
    StyledEngineProvider,
    useColorScheme
} from "@mui/joy";

const container = document.getElementById('root')

const root = createRoot(container!)

root.render(
    <CssVarsProvider>
        <StyledEngineProvider injectFirst>
            <React.StrictMode>
                <CssBaseline>
                    <App/>
                </CssBaseline>
            </React.StrictMode>
        </StyledEngineProvider>
    </CssVarsProvider>
)


