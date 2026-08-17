import { useEffect, useState } from "react";
import { getProperties } from "../api/properties";
import {
  getMaintenance,
  createMaintenance,
  startMaintenance,
  completeMaintenance,
  deleteMaintenance,
} from "../api/maintenance";

const emptyForm = {
  title: "",
  description: "",
  priority: "MEDIUM",
};

export default function Maintenance() {
  const [properties, setProperties] = useState([]);
  const [propertyId, setPropertyId] = useState("");
  const [requests, setRequests] = useState([]);
  const [form, setForm] = useState(emptyForm);

  useEffect(() => {
    loadProperties();
  }, []);

  async function loadProperties() {
    try {
      const data = await getProperties();
      setProperties(data);

      if (data.length) {
        setPropertyId(String(data[0].id));
      }
    } catch (error) {
      console.error(error);
    }
  }

  useEffect(() => {
    if (propertyId) {
      loadMaintenance(propertyId);
    }
  }, [propertyId]);

  async function loadMaintenance(id) {
    try {
      const data = await getMaintenance(id);
      setRequests(data);
    } catch (error) {
      console.error(error);
      setRequests([]);
    }
  }

  function update(field, value) {
    setForm((current) => ({
      ...current,
      [field]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    try {
      await createMaintenance(propertyId, form);

      setForm(emptyForm);
      await loadMaintenance(propertyId);
    } catch (error) {
      console.error(error);
      alert(
        error.response?.data?.message ||
          "Could not create maintenance request"
      );
    }
  }

  async function handleStart(id) {
    try {
      await startMaintenance(id);
      await loadMaintenance(propertyId);
    } catch (error) {
      console.error(error);
      alert("Could not start maintenance");
    }
  }

  async function handleComplete(id) {
    try {
      await completeMaintenance(id);
      await loadMaintenance(propertyId);
    } catch (error) {
      console.error(error);
      alert("Could not complete maintenance");
    }
  }

  async function handleDelete(id) {
    if (!confirm("Delete this maintenance request?")) {
      return;
    }

    try {
      await deleteMaintenance(id);
      await loadMaintenance(propertyId);
    } catch (error) {
      console.error(error);
      alert("Could not delete maintenance request");
    }
  }

  return (
    <div className="page">
      <h1>Maintenance</h1>

      {properties.length === 0 ? (
        <p>Create a property first.</p>
      ) : (
        <>
          <label>
            Property
            <select
              value={propertyId}
              onChange={(e) => setPropertyId(e.target.value)}
            >
              {properties.map((property) => (
                <option
                  key={property.id}
                  value={property.id}
                >
                  {property.nickname}
                </option>
              ))}
            </select>
          </label>

          <form
            onSubmit={handleSubmit}
            className="property-form"
          >
            <h2>Report Maintenance</h2>

            <label>
              Title
              <input
                value={form.title}
                onChange={(e) =>
                  update("title", e.target.value)
                }
                placeholder="Leaking tap"
                required
              />
            </label>

            <label>
              Description
              <textarea
                value={form.description}
                onChange={(e) =>
                  update(
                    "description",
                    e.target.value
                  )
                }
                placeholder="Describe the problem..."
                required
              />
            </label>

            <label>
              Priority
              <select
                value={form.priority}
                onChange={(e) =>
                  update("priority", e.target.value)
                }
              >
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
                <option value="URGENT">Urgent</option>
              </select>
            </label>

            <button type="submit">
              Report Maintenance
            </button>
          </form>

          <section>
            <h2>Maintenance Requests</h2>

            {requests.length === 0 ? (
              <p>No maintenance requests.</p>
            ) : (
              <div className="property-list">
                {requests.map((request) => (
                  <div
                    className="property-card"
                    key={request.id}
                  >
                    <h3>{request.title}</h3>

                    <p>
                      {request.description}
                    </p>

                    <p>
                      Priority:{" "}
                      <strong>
                        {request.priority}
                      </strong>
                    </p>

                    <p>
                      Status:{" "}
                      <strong>
                        {request.status}
                      </strong>
                    </p>

                    <p>
                      Reported:{" "}
                      {request.reportedDate}
                    </p>

                    {request.resolvedDate && (
                      <p>
                        Resolved:{" "}
                        {request.resolvedDate}
                      </p>
                    )}

                    {request.status === "OPEN" && (
                      <button
                        onClick={() =>
                          handleStart(request.id)
                        }
                      >
                        Start
                      </button>
                    )}

                    {request.status === "IN_PROGRESS" && (
                      <button
                        onClick={() =>
                          handleComplete(request.id)
                        }
                      >
                        Complete
                      </button>
                    )}

                    {request.status !== "COMPLETED" && (
                      <button
                        className="danger"
                        onClick={() =>
                          handleDelete(request.id)
                        }
                      >
                        Delete
                      </button>
                    )}
                  </div>
                ))}
              </div>
            )}
          </section>
        </>
      )}
    </div>
  );
}
