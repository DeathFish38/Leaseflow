import { useEffect, useState } from "react";
import {
  getProperties,
  createProperty,
  updateProperty,
  deleteProperty,
} from "../api/properties";

export default function Properties() {
  const [properties, setProperties] = useState([]);
  const [loading, setLoading] = useState(true);

  const [form, setForm] = useState({
    nickname: "",
    addressLine1: "",
    suburb: "",
    state: "NSW",
    postcode: "",
    moveInDate: "",
    moveOutDate: "",
  });

  const [editingId, setEditingId] = useState(null);

  async function loadProperties() {
    try {
      const data = await getProperties();
      setProperties(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadProperties();
  }, []);

  function update(field, value) {
    setForm({
      ...form,
      [field]: value,
    });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    const data = {
      ...form,
      moveInDate: form.moveInDate || null,
      moveOutDate: form.moveOutDate || null,
    };

    try {
      if (editingId) {
        await updateProperty(editingId, data);
      } else {
        await createProperty(data);
      }

      resetForm();
      await loadProperties();
    } catch (error) {
      console.error(error);
      alert("Could not save property");
    }
  }

  function editProperty(property) {
    setEditingId(property.id);

    setForm({
      nickname: property.nickname || "",
      addressLine1: property.addressLine1 || "",
      suburb: property.suburb || "",
      state: property.state || "NSW",
      postcode: property.postcode || "",
      moveInDate: property.moveInDate || "",
      moveOutDate: property.moveOutDate || "",
    });
  }

  async function removeProperty(id) {
    if (!confirm("Delete this property?")) {
      return;
    }

    try {
      await deleteProperty(id);
      await loadProperties();
    } catch (error) {
      console.error(error);
      alert("Could not delete property");
    }
  }

  function resetForm() {
    setEditingId(null);

    setForm({
      nickname: "",
      addressLine1: "",
      suburb: "",
      state: "NSW",
      postcode: "",
      moveInDate: "",
      moveOutDate: "",
    });
  }

  if (loading) {
    return <p>Loading...</p>;
  }

  return (
    <div className="page">
      <h1>Properties</h1>

      <form className="property-form" onSubmit={handleSubmit}>
        <h2>{editingId ? "Edit Property" : "Add Property"}</h2>

        <input
          placeholder="Nickname"
          value={form.nickname}
          onChange={(e) => update("nickname", e.target.value)}
          required
        />

        <input
          placeholder="Address"
          value={form.addressLine1}
          onChange={(e) => update("addressLine1", e.target.value)}
          required
        />

        <input
          placeholder="Suburb"
          value={form.suburb}
          onChange={(e) => update("suburb", e.target.value)}
          required
        />

        <input
          placeholder="State"
          value={form.state}
          onChange={(e) => update("state", e.target.value)}
          required
        />

        <input
          placeholder="Postcode"
          value={form.postcode}
          onChange={(e) => update("postcode", e.target.value)}
          required
        />

        <label>
          Move in
          <input
            type="date"
            value={form.moveInDate}
            onChange={(e) => update("moveInDate", e.target.value)}
          />
        </label>

        <label>
          Move out
          <input
            type="date"
            value={form.moveOutDate}
            onChange={(e) => update("moveOutDate", e.target.value)}
          />
        </label>

        <button type="submit">
          {editingId ? "Update Property" : "Add Property"}
        </button>

        {editingId && (
          <button type="button" onClick={resetForm}>
            Cancel
          </button>
        )}
      </form>

      <section>
        <h2>Your Properties</h2>

        {properties.length === 0 && <p>No properties yet.</p>}

        <div className="property-list">
          {properties.map((property) => (
            <div className="property-card" key={property.id}>
              <h3>{property.nickname}</h3>

              <p>
                {property.addressLine1}, {property.suburb}
              </p>

              <p>
                {property.state} {property.postcode}
              </p>

              <p>
                Move in: {property.moveInDate || "Not set"}
              </p>

              <div>
                <button onClick={() => editProperty(property)}>
                  Edit
                </button>

                <button
                  className="danger"
                  onClick={() => removeProperty(property.id)}
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}