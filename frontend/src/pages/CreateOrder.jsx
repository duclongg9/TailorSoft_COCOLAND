import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCustomers, getProductTypes, getMeasurementTypes, getMaterials, createOrder } from '../api';
import CurrencyInput from '../components/CurrencyInput';

const fmt = (n) => n != null ? Number(n).toLocaleString('vi-VN') + ' ₫' : '—';

const EMPTY_ITEM = {
  productTypeId: '', measurementTypes: [], measurements: {},
  materialId: '', materialName: '', unitPrice: '', quantity: 1, usedQuantity: '', note: '',
};

export default function CreateOrder() {
  const navigate = useNavigate();
  const [customers, setCustomers] = useState([]);
  const [productTypes, setProductTypes] = useState([]);
  const [materials, setMaterials] = useState([]);
  const [saving, setSaving] = useState(false);
  const [toast, setToast] = useState('');

  const [form, setForm] = useState({
    customerId: '',
    orderDate: new Date().toISOString().slice(0, 10),
    deliveryDate: '',
    total: '',
    deposit: '',
  });

  const [items, setItems] = useState([{ ...EMPTY_ITEM }]);

  useEffect(() => {
    getCustomers().then(r => setCustomers(r.data));
    getProductTypes().then(r => setProductTypes(r.data));
    getMaterials().then(r => setMaterials(r.data));
  }, []);

  const showToast = (m) => { setToast(m); setTimeout(() => setToast(''), 2500); };
  const F = (k) => (e) => setForm(f => ({ ...f, [k]: e.target.value }));

  const selectPT = async (i, ptId) => {
    const next = [...items];
    next[i].productTypeId = ptId;
    if (!ptId) { next[i].measurementTypes = []; setItems(next); return; }
    const r = await getMeasurementTypes(ptId);
    next[i].measurementTypes = r.data;
    next[i].measurements = {};
    setItems(next);
  };

  const selectMat = (i, matId) => {
    const mat = materials.find(m => m.id === Number(matId));
    const next = [...items];
    next[i].materialId = matId;
    next[i].materialName = mat?.name || '';
    next[i].unitPrice = mat?.price || '';
    setItems(next);
  };

  const updateItem = (i, k, v) => {
    const next = [...items];
    next[i][k] = v;
    setItems(next);
  };

  const updateMeasure = (i, mtId, v) => {
    const next = [...items];
    next[i].measurements = { ...next[i].measurements, [mtId]: v };
    setItems(next);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.customerId) { showToast('Vui lòng chọn khách hàng'); return; }
    if (!confirm('Xác nhận tạo đơn hàng này?')) return;
    setSaving(true);

    const payload = {
      ...form,
      customerId: Number(form.customerId),
      total: Number(form.total) || 0,
      deposit: Number(form.deposit) || 0,
      details: items.map(it => ({
        productType: productTypes.find(p => p.id === Number(it.productTypeId))?.name || '',
        materialId: it.materialId ? Number(it.materialId) : null,
        materialName: it.materialName,
        unitPrice: Number(it.unitPrice) || 0,
        quantity: Number(it.quantity),
        usedQuantity: Number(it.usedQuantity) || 0,
        note: it.note,
        measurements: Object.entries(it.measurements)
          .filter(([, v]) => v !== '')
          .map(([mtId, val]) => ({
            productTypeId: Number(it.productTypeId),
            measurementTypeId: Number(mtId),
            value: Number(val),
          })),
      })),
    };

    try {
      const r = await createOrder(payload);
      showToast('Tạo đơn thành công!');
      navigate(`/orders/${r.data.id}`);
    } catch { showToast('Lỗi khi tạo đơn'); setSaving(false); }
  };

  return (
    <>
      <div className="page-hdr">
        <h2>Tạo đơn hàng mới</h2>
      </div>

      <form onSubmit={handleSubmit}>
        {/* Basic info */}
        <div className="card card-pad" style={{ marginBottom: 20 }}>
          <div className="card-title">Thông tin khách hàng & thời gian</div>
          <div className="form-group">
            <label className="form-label">Chọn khách hàng *</label>
            <select className="form-input" required value={form.customerId} onChange={F('customerId')}>
              <option value="">— Chọn khách hàng —</option>
              {customers.map(c => <option key={c.id} value={c.id}>{c.name}  ·  {c.phone}</option>)}
            </select>
          </div>
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Ngày đặt</label>
              <input className="form-input" type="date" value={form.orderDate} onChange={F('orderDate')} />
            </div>
            <div className="form-group">
              <label className="form-label">Ngày hẹn giao</label>
              <input className="form-input" type="date" value={form.deliveryDate} onChange={F('deliveryDate')} />
            </div>
          </div>
          <div className="form-row">
            <CurrencyInput
              label="Tổng tiền đơn (₫)"
              value={form.total}
              onChange={val => setForm(f => ({ ...f, total: val }))}
            />
            <CurrencyInput
              label="Tiền đặt cọc (₫)"
              value={form.deposit}
              onChange={val => setForm(f => ({ ...f, deposit: val }))}
            />
          </div>
        </div>

        {/* Items */}
        {items.map((item, i) => (
          <div className="card card-pad" key={i} style={{ marginBottom: 20 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
              <div className="card-title" style={{ marginBottom: 0, paddingBottom: 0, borderBottom: 'none' }}>
                Sản phẩm #{i + 1}
              </div>
              {items.length > 1 && (
                <button type="button" className="btn btn-danger btn-xs"
                  onClick={() => setItems(items.filter((_, idx) => idx !== i))}>Xoá</button>
              )}
            </div>

            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Loại sản phẩm</label>
                <select className="form-input" value={item.productTypeId} onChange={e => selectPT(i, e.target.value)}>
                  <option value="">— Chọn loại —</option>
                  {productTypes.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">Số lượng</label>
                <input className="form-input" type="number" min="1" value={item.quantity}
                  onChange={e => updateItem(i, 'quantity', e.target.value)} />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Chất liệu vải</label>
                <select className="form-input" value={item.materialId} onChange={e => selectMat(i, e.target.value)}>
                  <option value="">— Chọn vải —</option>
                  {materials.map(m => <option key={m.id} value={m.id}>{m.name} · {m.color}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label className="form-label">Số mét vải dùng</label>
                <input className="form-input" type="number" step="0.1" value={item.usedQuantity}
                  onChange={e => updateItem(i, 'usedQuantity', e.target.value)} />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Ghi chú may</label>
              <textarea className="form-input" rows={2} value={item.note}
                onChange={e => updateItem(i, 'note', e.target.value)}
                placeholder="Form rộng, xẻ tà, cổ đứng…" />
            </div>

            {item.measurementTypes.length > 0 && (
              <div style={{ marginTop: 10, padding: 20, background: 'var(--bg-warm)', borderRadius: 10 }}>
                <span className="section-label">Thông số đo (cm)</span>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(120px, 1fr))', gap: 12 }}>
                  {item.measurementTypes.map(mt => (
                    <div key={mt.id}>
                      <label className="form-label">{mt.name}</label>
                      <input className="form-input" type="number" step="0.5" placeholder="0"
                        value={item.measurements[mt.id] || ''}
                        onChange={e => updateMeasure(i, mt.id, e.target.value)} />
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        ))}

        <button type="button" className="btn btn-ghost"
          style={{ width: '100%', marginBottom: 24, borderStyle: 'dashed' }}
          onClick={() => setItems([...items, { ...EMPTY_ITEM }])}>
          + Thêm sản phẩm
        </button>

        {/* Sticky footer */}
        <div style={{ position: 'sticky', bottom: 16, display: 'flex', gap: 12, background: 'var(--bg)', padding: '16px 0', borderTop: '1px solid var(--border)' }}>
          <button type="button" className="btn btn-ghost" style={{ flex: 1 }} onClick={() => navigate('/orders')}>Huỷ bỏ</button>
          <button type="submit" className="btn btn-primary" style={{ flex: 2 }} disabled={saving}>
            {saving ? 'Đang xử lý…' : '✓ Xác nhận tạo đơn'}
          </button>
        </div>
      </form>
      {toast && <div className="toast">{toast}</div>}
    </>
  );
}
