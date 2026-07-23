import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router'
import {AppRoutes} from "./routes/Routes.jsx";
import { UserProvider} from "./UserContext.jsx";
import 'bootstrap-icons/font/bootstrap-icons.css';




createRoot(document.getElementById("root")).render(
    <StrictMode>
        <BrowserRouter>
            <UserProvider>
                <AppRoutes />
            </UserProvider>
        </BrowserRouter>
    </StrictMode>

);