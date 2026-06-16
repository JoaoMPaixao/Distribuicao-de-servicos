import Sidebar from '../components/Sidebar'
import Topbar from '../components/Topbar'
import '../styles/estilos.css'

function DashboardLayout({ children, titulo }) {
    return (
        <>
            <Sidebar />
            <Topbar titulo={titulo} />
            <main className="main">
                <div className="main-content">
                    {children}
                </div>
            </main>
        </>
    )
}

export default DashboardLayout