import { useEffect, useState } from "react";
import {
  getLease,
  createLease,
  updateLease,
  deleteLease,
} from "../api/leases";
import { getProperties } from "../api/properties";

const emptyForm = {
  leaseStart: "",
  leaseEnd: "",
  weeklyRent: "",
  bondAmount: "",
  paymentFrequency: "WEEKLY",
  inspectionFrequency: "",
  notes: "",
};

export default function Leases() {
  const [properties, setProperties] = useState([]);
  const [propertyId, setPropertyId] = useState("");
  const [lease, setLease] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadProperties();
  }, []);

  async function loadProperties() {
    try {
      const data = await getProperties();
      setProperties(data);

      if (data.length > 0) {
        setPropertyId(String(data[0].id));
      }
    } catch (error) {
      console.error(error);
      alert("Could not load properties");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (propertyId) {
      loadLease(propertyId);
    }
  }, [propertyId]);

  async function loadLease(id) {
    try {
      const data = await getLease(id);
      setLease(data);

      setForm({
        leaseStart: data.leaseStart || "",
        leaseEnd: data.leaseEnd || "",
        weeklyRent: data.weeklyRent || "",
        bondAmount: data.bondAmount || "",
        paymentFrequency: data.paymentFrequency || "WEEKLY",
        inspectionFrequency: data.inspectionFrequency || "",
        notes: data.notes || "",
      });
    } catch (error) {
      if (error.response?.status === 404) {
        setLease(null);
        setForm(emptyForm);
      } else {
        console.error(error);
      }
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

    const data = {
      ...form,
      weeklyRent: Number(form.weeklyRent),
      bondAmount: Number(form.bondAmount),
    };

    try {
      if (lease) {
        const updated = await updateLease(lease.id, data);
        setLease(updated);
        alert("Lease updated");
      } else {
        const created = await createLease(propertyId, data);
        setLease(created);
        alert("Lease created");
      }
    } catch (error) {
      console.error(error);
      alert(
        error.response?.data?.message ||
          "Could not save lease"
      );
    }
  }

  async function handleDelete() {
    if (!lease) return;

    if (!confirm("Delete this lease?")) return;

    try {
      await deleteLease(lease.id);
      setLease(null);
      setForm(emptyForm);
    } catch (error) {
      console.error(error);
      alert("Could not delete lease");
    }
  }

  if (loading) {
    return <div className="page"><p>Loading...</p></div>;
  }

  return (
    <div className="page">
      <h1>Leases</h1>

      {properties.length === 0 ? (
        <p>
          Create a property first before creating a lease.
        </p>
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

          <form onSubmit={handleSubmit} className="property-form">
            <h2>
              {lease ? "Edit Lease" : "Create Lease"}
            </h2>

            <label>
              Lease start
              <input
                type="date"
                value={form.leaseStart}
                onChange={(e) =>
                  update("leaseStart", e.target.value)
                }
                required
              />
            </label>

            <label>
              Lease end
              <input
                type="date"
                value={form.leaseEnd}
                onChange={(e) =>
                  update("leaseEnd", e.target.value)
                }
                required
              />
            </label>

            <label>
              Weekly rent
              <input
                type="number"
                min="0.01"
                step="0.01"
                value={form.weeklyRent}
                onChange={(e) =>
                  update("weeklyRent", e.target.value)
                }
                required
              />
            </label>

            <label>
              Bond amount
              <input
                type="number"
                min="0.01"
                step="0.01"
                value={form.bondAmount}
                onChange={(e) =>
                  update("bondAmount", e.target.value)
                }
                required
              />
            </label>

            <label>
              Payment frequency
              <select
                value={form.paymentFrequency}
                onChange={(e) =>
                  update(
                    "paymentFrequency",
                    e.target.value
                  )
                }
              >
                <option value="WEEKLY">Weekly</option>
                <option value="FORTNIGHTLY">
                  Fortnightly
                </option>
                <option value="MONTHLY">Monthly</option>
              </select>
            </label>

            <label>
              Inspection frequency
              <input
                value={form.inspectionFrequency}
                onChange={(e) =>
                  update(
                    "inspectionFrequency",
                    e.target.value
                  )
                }
                placeholder="e.g. Every 3 months"
              />
            </label>

            <label>
              Notes
              <textarea
                value={form.notes}
                onChange={(e) =>
                  update("notes", e.target.value)
                }
              />
            </label>

            <button type="submit">
              {lease ? "Update Lease" : "Create Lease"}
            </button>

            {lease && (
              <button
                type="button"
                className="danger"
                onClick={handleDelete}
              >
                Delete Lease
              </button>
            )}
          </form>

          {lease && (
            <section className="property-card">
              <h2>Current Lease</h2>

              <p>
                <strong>Rent:</strong>{" "}
                ${lease.weeklyRent} / week
              </p>

              <p>
                <strong>Bond:</strong> ${lease.bondAmount}
              </p>

              <p>
                <strong>Period:</strong>{" "}
                {lease.leaseStart} → {lease.leaseEnd}
              </p>

              <p>
                <strong>Frequency:</strong>{" "}
                {lease.paymentFrequency}
              </p>
            </section>
          )}
        </>
      )}
    </div>
  );
}
