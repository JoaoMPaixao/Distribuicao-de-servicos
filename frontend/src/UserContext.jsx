import { useState, useEffect, createContext } from "react";
import { me } from "./api/loginApi.jsx";

export const UserContext = createContext(undefined);

export const UserProvider = ({ children }) => {
    const [user, setUser] = useState(undefined);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                // Só chama se não estivermos na página de login
                if (window.location.pathname !== "/") {
                    const userData = await me();
                    setUser(userData);
                }
            } catch (e) {
                setUser(null); // não autenticado
            }
        };

        fetchUser();
    }, []);

    return (
        <UserContext.Provider value={{ user, setUser }}>
            {children}
        </UserContext.Provider>
    );
};