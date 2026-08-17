import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem("user") || "{}");

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/");
  }

  return (
    <div className="page">
      <header className="topbar">
        <div>
          <h1>Good to see you</h1>
          <p className="dashboard-intro">Here’s your rental portfolio at a glance.</p>
        </div>

        <button onClick={logout}>Logout</button>
      </header>

      <main>
        <p className="dashboard-intro">Signed in as {user.email || "your account"}</p>

        <div className="grid">
          <div className="dashboard-card">
            <h3>Properties</h3>
            <p>Manage your properties</p>
            <button onClick={() => navigate("/properties")}>
              View Properties
            </button>
          </div>

          <div className="dashboard-card">
            <h3>Leases</h3>
            <p>Manage your leases</p>
            <button onClick={() => navigate("/leases")}>View Leases</button>
          </div>

          <div className="dashboard-card">
            <h3>Payments</h3>
            <p>Track rental payments</p>
            <button onClick={() => navigate("/payments")}>View Payments</button>
          </div>

          <div className="dashboard-card">
            <h3>Maintenance</h3>
            <p>Manage maintenance requests</p>
            <button onClick={() => navigate("/maintenance")}>View Maintenance</button>
          </div>
        </div>
      </main>
    </div>
  );
}
