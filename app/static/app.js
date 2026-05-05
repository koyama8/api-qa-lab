const state = {
  clientes: [],
  cartoes: [],
  faturas: [],
};

const apiStatus = document.querySelector("#apiStatus");
const tokenStatus = document.querySelector("#tokenStatus");
const loginMessage = document.querySelector("#loginMessage");
const clienteMessage = document.querySelector("#clienteMessage");
const clientesList = document.querySelector("#clientesList");
const cartoesList = document.querySelector("#cartoesList");
const faturasList = document.querySelector("#faturasList");
const clientesCount = document.querySelector("#clientesCount");
const cartoesCount = document.querySelector("#cartoesCount");
const faturasCount = document.querySelector("#faturasCount");

async function api(path, options = {}) {
  const response = await fetch(path, {
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      ...options.headers,
    },
    ...options,
  });

  const body = response.status === 204 ? null : await response.json();

  if (!response.ok) {
    throw new Error(body?.message || "Erro ao chamar a API.");
  }

  return body;
}

function money(value) {
  return Number(value).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
}

function statusClass(status) {
  if (["ATIVO", "PAGA", "APROVADO"].includes(status)) return "ok";
  if (["BLOQUEADO", "ABERTA", "FECHADA"].includes(status)) return "warn";
  return "danger";
}

async function loadDashboard() {
  try {
    const [health, clientes, cartoes, faturas] = await Promise.all([
      api("/health"),
      api("/clientes"),
      api("/cartoes"),
      api("/faturas"),
    ]);

    apiStatus.textContent = `${health.data.service} ${health.data.status}`;
    state.clientes = clientes.data;
    state.cartoes = cartoes.data;
    state.faturas = faturas.data;
    render();
  } catch (error) {
    apiStatus.textContent = "Indisponível";
  }
}

function render() {
  clientesCount.textContent = state.clientes.length;
  cartoesCount.textContent = state.cartoes.length;
  faturasCount.textContent = state.faturas.length;
  renderClientes();
  renderCartoes();
  renderFaturas();
}

function renderClientes() {
  clientesList.innerHTML = state.clientes
    .map(
      (cliente) => `
        <div class="item">
          <div class="item-head">
            <span class="item-title">${cliente.nome}</span>
            <span class="pill ${cliente.ativo ? "ok" : "danger"}">
              ${cliente.ativo ? "ATIVO" : "INATIVO"}
            </span>
          </div>
          <div class="details">
            <span>ID: ${cliente.id}</span>
            <span>E-mail: ${cliente.email}</span>
            <span>CPF: ${cliente.cpf}</span>
          </div>
        </div>
      `,
    )
    .join("");
}

function renderCartoes() {
  cartoesList.innerHTML = state.cartoes
    .map(
      (cartao) => `
        <div class="item">
          <div class="item-head">
            <span class="item-title">${cartao.numero_masked}</span>
            <span class="pill ${statusClass(cartao.status)}">${cartao.status}</span>
          </div>
          <div class="details">
            <span>ID: ${cartao.id}</span>
            <span>Cliente ID: ${cartao.cliente_id}</span>
            <span>Limite: ${money(cartao.limite)}</span>
          </div>
          <div class="inline-actions">
            <button type="button" data-action="bloquear" data-id="${cartao.id}">Bloquear</button>
            <button type="button" data-action="desbloquear" data-id="${cartao.id}">Desbloquear</button>
            <button type="button" data-action="cancelar" data-id="${cartao.id}">Cancelar</button>
          </div>
        </div>
      `,
    )
    .join("");
}

function renderFaturas() {
  faturasList.innerHTML = state.faturas
    .map(
      (fatura) => `
        <div class="item">
          <div class="item-head">
            <span class="item-title">Fatura #${fatura.id}</span>
            <span class="pill ${statusClass(fatura.status)}">${fatura.status}</span>
          </div>
          <div class="details">
            <span>Cliente ID: ${fatura.cliente_id}</span>
            <span>Cartão ID: ${fatura.cartao_id}</span>
            <span>Valor: ${money(fatura.valor)}</span>
            <span>Vencimento: ${fatura.vencimento}</span>
          </div>
          <div class="inline-actions">
            <button type="button" data-action="pagar" data-id="${fatura.id}" data-valor="${fatura.valor}">
              Pagar
            </button>
          </div>
        </div>
      `,
    )
    .join("");
}

document.querySelector("#loginForm").addEventListener("submit", async (event) => {
  event.preventDefault();
  loginMessage.className = "message";
  loginMessage.textContent = "Enviando login...";

  const form = new FormData(event.currentTarget);

  try {
    const result = await api("/login", {
      method: "POST",
      body: JSON.stringify({
        usuario: form.get("usuario"),
        senha: form.get("senha"),
      }),
    });
    tokenStatus.textContent = result.data.token;
    loginMessage.textContent = "Login válido. Token fake gerado.";
  } catch (error) {
    loginMessage.className = "message error";
    loginMessage.textContent = error.message;
  }
});

document.querySelector("#clienteForm").addEventListener("submit", async (event) => {
  event.preventDefault();
  clienteMessage.className = "message";
  clienteMessage.textContent = "Criando cliente...";

  const form = new FormData(event.currentTarget);

  try {
    await api("/clientes", {
      method: "POST",
      body: JSON.stringify({
        nome: form.get("nome"),
        email: form.get("email"),
        cpf: form.get("cpf"),
      }),
    });
    clienteMessage.textContent = "Cliente criado com sucesso.";
    await loadDashboard();
  } catch (error) {
    clienteMessage.className = "message error";
    clienteMessage.textContent = error.message;
  }
});

document.querySelector("#resetButton").addEventListener("click", async () => {
  await api("/reset", { method: "POST" });
  tokenStatus.textContent = "Ainda não gerado";
  loginMessage.textContent = "";
  clienteMessage.textContent = "";
  await loadDashboard();
});

cartoesList.addEventListener("click", async (event) => {
  const button = event.target.closest("button[data-action]");
  if (!button) return;

  try {
    await api(`/cartoes/${button.dataset.id}/${button.dataset.action}`, {
      method: "PUT",
    });
    await loadDashboard();
  } catch (error) {
    alert(error.message);
  }
});

faturasList.addEventListener("click", async (event) => {
  const button = event.target.closest("button[data-action='pagar']");
  if (!button) return;

  try {
    await api("/pagamentos", {
      method: "POST",
      body: JSON.stringify({
        fatura_id: Number(button.dataset.id),
        valor: Number(button.dataset.valor),
      }),
    });
    await loadDashboard();
  } catch (error) {
    alert(error.message);
  }
});

loadDashboard();
