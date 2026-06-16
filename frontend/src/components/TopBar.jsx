import {UserContext} from "../UserContext.jsx";
import {useContext} from "react";

function Topbar({ titulo }) {
   const { user } = useContext(UserContext)

    return (
        <div className="topbar">
            <div className="topbar-left">
                <h4>{titulo}</h4>
            </div>
            <div className="topbar-right">
                <div className="user-chip">
                    <div className="user-info">
                        <div className="name">{user?.nome ?? 'João Paixão'}</div>
                        <div className="role">{user?.role ?? 'Administrador'}</div>
                    </div>
                    <i className="bi bi-chevron-down chevron-down"></i>
                </div>
            </div>
        </div>
    )
}

export default Topbar