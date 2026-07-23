import Sidebar from '../components/Sidebar'
import Topbar from '../components/Topbar'


function DashboardLayout({ children}) {
    return (
        <>
            <Sidebar />
            <Topbar />
            <main className="main">
                <div className="main-content">
                    {children}
                </div>
            </main>
        </>
    )
}

export default DashboardLayout