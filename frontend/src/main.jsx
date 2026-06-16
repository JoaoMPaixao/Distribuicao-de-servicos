import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router'
import {AppRoutes} from "./routes/Routes.jsx";
import {UserContext} from "./UserContext.jsx";
import 'bootstrap-icons/font/bootstrap-icons.css';

const utilizadorSimulado = {
    id: 2,
    nome: "João Paixão",
    role: "Administrador",
    departamento_id:1

};

createRoot(document.getElementById("root")).render(
    <StrictMode>
        <BrowserRouter>
            {/* Alterar depois que autenticação estiver feita, apagar .provider...*/}
            <UserContext.Provider value={{ user: utilizadorSimulado }}>
                <AppRoutes />
            </UserContext.Provider>
        </BrowserRouter>
    </StrictMode>

);