import { Navigate, Route, Routes } from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Properties from "./pages/Properties";
import Leases from "./pages/Leases";
import Payments from "./pages/Payments";
import Maintenance from "./pages/Maintenance";

import Layout from "./components/Layout";

function ProtectedRoute({ children }) {
  const token = localStorage.getItem("token");

  if (!token) {
    return <Navigate to="/" replace />;
  }

  return children;
}

function Protected({ children }) {
  return (
    <ProtectedRoute>
      {children}
    </ProtectedRoute>
  );
}

export default function App() {
  return (
    <Routes>
      {/* Public pages */}
      <Route path="/" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* Protected pages with Sidebar */}
      <Route
        element={
          <Protected>
            <Layout />
          </Protected>
        }
      >
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/properties" element={<Properties />} />
        <Route path="/leases" element={<Leases />} />
        <Route path="/payments" element={<Payments />} />
        <Route path="/maintenance" element={<Maintenance />} />
      </Route>

      {/* Unknown route */}
      <Route
        path="*"
        element={<Navigate to="/dashboard" replace />}
      />
    </Routes>
  );
}