import { useEffect, useState } from "react";
import { getProperties } from "../api/properties";
import { getLease } from "../api/leases";
import {
  getPayments,
  createPayment,
  markPaymentPaid,
  deletePayment,
} from "../api/payments";

const emptyForm = {
  amount: "",
  dueDate: "",
  paymentMethod: "BANK_TRANSFER",
  reference: "",
  notes: "",
};

export default function Payments() {
  const [properties, setProperties] = useState([]);
  const [propertyId, setPropertyId] = useState("");
  const [lease, setLease] = useState(null);
  const [payments, setPayments] = useState([]);
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
      loadLeaseAndPayments(propertyId);
    }
  }, [propertyId]);

  async function loadLeaseAndPayments(id) {
    try {
      const leaseData = await getLease(id);
      setLease(leaseData);

      const paymentData = await getPayments(leaseData.id);
      setPayments(paymentData);
    } catch (error) {
      setLease(null);
      setPayments([]);
      console.error(error);
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

    if (!lease) {
      alert("This property has no lease.");
      return;
    }

    try {
      await createPayment(lease.id, {
        ...form,
        amount: Number(form.amount),
      });

      setForm(emptyForm);
      await loadLeaseAndPayments(propertyId);
    } catch (error) {
      console.error(error);
      alert(
        error.response?.data?.message ||
          "Could not create payment"
      );
    }
  }

  async function handlePaid(id) {
    try {
      await markPaymentPaid(id);
      await loadLeaseAndPayments(propertyId);
    } catch (error) {
      console.error(error);
      alert("Could not mark payment as paid");
    }
  }

  async function handleDelete(id) {
    if (!confirm("Delete this payment?")) return;

    try {
      await deletePayment(id);
      await loadLeaseAndPayments(propertyId);
    } catch (error) {
      console.error(error);
      alert("Could not delete payment");
    }
  }

  return (
    <div className="page">
      <h1>Payments</h1>

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

          {!lease ? (
            <p>No lease exists for this property.</p>
          ) : (
            <>
              <form
                onSubmit={handleSubmit}
                className="property-form"
              >
                <h2>Add Payment</h2>

                <label>
                  Amount
                  <input
                    type="number"
                    min="0.01"
                    step="0.01"
                    value={form.amount}
                    onChange={(e) =>
                      update("amount", e.target.value)
                    }
                    required
                  />
                </label>

                <label>
                  Due date
                  <input
                    type="date"
                    value={form.dueDate}
                    onChange={(e) =>
                      update("dueDate", e.target.value)
                    }
                    required
                  />
                </label>

                <label>
                  Payment method
                  <select
                    value={form.paymentMethod}
                    onChange={(e) =>
                      update(
                        "paymentMethod",
                        e.target.value
                      )
                    }
                  >
                    <option value="BANK_TRANSFER">
                      Bank transfer
                    </option>
                    <option value="BPAY">BPAY</option>
                    <option value="CASH">Cash</option>
                    <option value="CARD">Card</option>
                  </select>
                </label>

                <label>
                  Reference
                  <input
                    value={form.reference}
                    onChange={(e) =>
                      update("reference", e.target.value)
                    }
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
                  Add Payment
                </button>
              </form>

              <section>
                <h2>Payment History</h2>

                {payments.length === 0 ? (
                  <p>No payments yet.</p>
                ) : (
                  <div className="property-list">
                    {payments.map((payment) => (
                      <div
                        className="property-card"
                        key={payment.id}
                      >
                        <h3>
                          ${payment.amount}
                        </h3>

                        <p>
                          Due: {payment.dueDate}
                        </p>

                        <p>
                          Status:{" "}
                          <strong>
                            {payment.status}
                          </strong>
                        </p>

                        <p>
                          Method:{" "}
                          {payment.paymentMethod}
                        </p>

                        {payment.paidDate && (
                          <p>
                            Paid: {payment.paidDate}
                          </p>
                        )}

                        {payment.reference && (
                          <p>
                            Reference:{" "}
                            {payment.reference}
                          </p>
                        )}

                        {payment.status !== "PAID" && (
                          <button
                            onClick={() =>
                              handlePaid(payment.id)
                            }
                          >
                            Mark Paid
                          </button>
                        )}

                        <button
                          className="danger"
                          onClick={() =>
                            handleDelete(payment.id)
                          }
                        >
                          Delete
                        </button>
                      </div>
                    ))}
                  </div>
                )}
              </section>
            </>
          )}
        </>
      )}
    </div>
  );
}
