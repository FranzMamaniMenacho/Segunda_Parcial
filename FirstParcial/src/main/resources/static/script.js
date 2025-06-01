const API_BASE = 'http://localhost:8080';
let currentEntity = 'clientes';
let currentOperation = null;

const entityConfig = {
    clientes: {
        fields: [
            { id: 'id', label: 'ID', type: 'number', required: true },
            { id: 'nombres', label: 'Nombres', type: 'text' },
            { id: 'apellidos', label: 'Apellidos', type: 'text' },
            { id: 'ci', label: 'CI', type: 'text' },
            { id: 'telefono', label: 'Teléfono', type: 'text' }
        ]
    },
    habitaciones: {
        fields: [
            { id: 'id', label: 'ID', type: 'number', required: true },
            { id: 'tipoHabitacion', label: 'Tipo', type: 'text' },
            { id: 'estado', label: 'Estado', type: 'text' },
            { id: 'precioNoche', label: 'Precio/Noche', type: 'number' }
        ]
    },
    pagos: {
        fields: [
            { id: 'id', label: 'ID', type: 'number', required: true },
            { id: 'metodo', label: 'Método', type: 'text' },
            { id: 'estado', label: 'Estado', type: 'text' },
            { id: 'monto', label: 'Monto', type: 'number' },
            { id: 'date', label: 'Fecha', type: 'date' }
        ]
    },
    parqueos: {
        fields: [
            { id: 'id', label: 'ID', type: 'number', required: true },
            { id: 'estado', label: 'Estado', type: 'text' },
            { id: 'precioPorNoche', label: 'Precio/Noche', type: 'number' },
            { id: 'marca', label: 'Marca', type: 'text' },
            { id: 'color', label: 'Color', type: 'text' },
            { id: 'placa', label: 'Placa', type: 'text' }
        ]
    },
    personal: {
        fields: [
            { id: 'id', label: 'ID', type: 'number', required: true },
            { id: 'nombre', label: 'Nombre', type: 'text' },
            { id: 'rol', label: 'Rol', type: 'text' },
            { id: 'CI', label: 'CI', type: 'text' }
        ]
    },
    reservas: {
        fields: [
            { id: 'id', label: 'ID', type: 'number', required: true },
            { id: 'clienteId', label: 'ID Cliente', type: 'number', required: true },
            { id: 'habitacionId', label: 'ID Habitación', type: 'number', required: true },
            { id: 'fechaEntrada', label: 'Fecha Entrada', type: 'date', required: true },
            { id: 'fechaSalida', label: 'Fecha Salida', type: 'date', required: true },
            { id: 'pagoId', label: 'ID Pago', type: 'number', required: true }
        ]
    }
};

function changeEntity(entity) {
    currentEntity = entity;
    document.querySelectorAll('.tab-button').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');
    hideForm();
    clearOutput();
}

async function getAll() {
    try {
        const response = await fetch(`${API_BASE}/${currentEntity}`);
        const data = await response.json();
        displayResult(`Buscar Todos los ${currentEntity}:`, data);
    } catch (error) {
        displayError('Error en Buscar Todos:', error);
    }
}

async function getById() {
    const id = prompt(`Ingrese el ID del ${currentEntity.substring(0, currentEntity.length-1)} a consultar:`);
    if (!id) return;

    try {
        const response = await fetch(`${API_BASE}/${currentEntity}/${id}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const data = await response.json();
        displayResult(`GET ${currentEntity.substring(0, currentEntity.length-1)} ID ${id}:`, data);
    } catch (error) {
        displayError(`Error en Buscar por ID ${id}:`, error);
    }
}

function showCreateForm() {
    setupForm('POST', 'Crear');
}

function showUpdateForm() {
    setupForm('PUT', 'Actualizar');
}

function showPatchForm() {
    setupForm('PATCH', 'Modificar');
}

function setupForm(operation, title) {
    currentOperation = operation;
    const formPanel = document.getElementById('formPanel');
    const formTitle = document.getElementById('formTitle');
    const dataForm = document.getElementById('dataForm');

    dataForm.innerHTML = '';

    formTitle.textContent = `${title} ${currentEntity.substring(0, currentEntity.length-1)} (${operation})`;

    entityConfig[currentEntity].fields.forEach(field => {
        const div = document.createElement('div');
        div.innerHTML = `
            <label for="${field.id}">${field.label}:</label>
            <input type="${field.type}" id="${field.id}"
                   ${field.required ? 'required' : ''}
                   ${field.type === 'date' ? 'value="' + new Date().toISOString().substring(0, 10) + '"' : ''}>
        `;
        dataForm.appendChild(div);
    });

    const submitBtn = document.getElementById('submitBtn');
    submitBtn.textContent = title;
    submitBtn.onclick = operation === 'POST' ? createItem :
                       operation === 'PUT' ? updateItem :
                       patchItem;

    formPanel.style.display = 'block';
}

async function createItem() {
    const itemData = getFormData();
    console.log('Datos a enviar:', itemData);

    try {
        const response = await fetch(`${API_BASE}/${currentEntity}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(itemData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Error en la solicitud');
        }

        const data = await response.json();
        displayResult(`${currentEntity.substring(0, currentEntity.length-1)} creado (POST):`, data);
        hideForm();
    } catch (error) {
        displayError('Error en agregar:', error);
    }
}

async function updateItem() {
    const itemData = getFormData();

    try {
        const response = await fetch(`${API_BASE}/${currentEntity}/${itemData.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(itemData)
        });

        if (!response.ok) throw new Error(await response.text());
        const data = await response.json();
        displayResult(`${currentEntity.substring(0, currentEntity.length-1)} actualizado (PUT):`, data);
        hideForm();
    } catch (error) {
        displayError('Error en actualizar:', error);
    }
}

async function patchItem() {
    const partialData = getFormData(true);

    try {
        const response = await fetch(`${API_BASE}/${currentEntity}/${partialData.id}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(partialData)
        });

        if (!response.ok) throw new Error(await response.text());
        const data = await response.json();
        displayResult(`${currentEntity.substring(0, currentEntity.length-1)} modificado (PATCH):`, data);
        hideForm();
    } catch (error) {
        displayError('Error en actualizar parcial:', error);
    }
}

function deletePrompt() {
    const id = prompt(`Ingrese el ID del ${currentEntity.substring(0, currentEntity.length-1)} a eliminar:`);
    if (!id) return;

    if (confirm(`¿Está seguro de eliminar el ${currentEntity.substring(0, currentEntity.length-1)} con ID ${id}?`)) {
        deleteItem(id);
    }
}

async function deleteItem(id) {
    try {
        const response = await fetch(`${API_BASE}/${currentEntity}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error(await response.text());
        displayResult(`${currentEntity.substring(0, currentEntity.length-1)} eliminado (DELETE):`, { id, status: 'Eliminado' });
    } catch (error) {
        displayError(`Error en DELETE ID ${id}:`, error);
    }
}

function getFormData(partial = false) {
    const data = {};
    entityConfig[currentEntity].fields.forEach(field => {
        const input = document.getElementById(field.id);
        if (!input) return;

        let value = input.value;
        if (value === '' && !field.required) return;
        switch(field.type) {
            case 'number':
                value = value ? parseInt(value) : 0;
                break;
            case 'date':
                value = value || new Date().toISOString().split('T')[0];
                break;
        }

        if (!partial || (value !== undefined && value !== '')) {
            data[field.id] = value;
        }
    });
    return data;
}

function displayResult(title, data) {
    const output = document.getElementById('output');
    output.innerHTML = `<strong>${title}</strong>\n${JSON.stringify(data, null, 2)}`;
    output.style.color = 'black';
}

function displayError(title, error) {
    const output = document.getElementById('output');
    output.innerHTML = `<strong style="color:red">${title}</strong>\n${error.message || error}`;
    console.error(error);
}

function hideForm() {
    document.getElementById('formPanel').style.display = 'none';
}

function clearOutput() {
    document.getElementById('output').innerHTML = '';
}

document.addEventListener('DOMContentLoaded', () => {
    console.log('Sistema inicializado');
});