const taskList = document.getElementById("task-list");

const taskForm = document.getElementById("task-form");
const taskNameInput = document.getElementById("task-name");
const dueDateInput = document.getElementById("due-date");
const priorityInput = document.getElementById("priority");

taskForm.addEventListener("submit", handleAddTask);

async function handleAddTask(event) {

    event.preventDefault();

    const newTask = {
        name: taskNameInput.value,
        dueDate: dueDateInput.value,
        priority: priorityInput.value
    };

    try {

        const response = await fetch("/api/tasks", {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(newTask)
        });

        if (!response.ok) {
            throw new Error("Failed to create task");
        }

        taskForm.reset();

        await loadTasks();

    } catch (error) {

        console.error(
            "Error creating task:",
            error
        );
    }
}

async function loadTasks() {

    try {

        const response =
                await fetch("/api/tasks");

        if (!response.ok) {
            throw new Error(
                "Failed to load tasks"
            );
        }

        const tasks =
                await response.json();

        renderTasks(tasks);

    } catch (error) {

        console.error(
            "Error loading tasks:",
            error
        );

        taskList.innerHTML =
                "<p>Could not load tasks.</p>";
    }
}

function renderTasks(tasks) {

    taskList.innerHTML = "";

    if (tasks.length === 0) {
        taskList.innerHTML =
            "<p>No tasks yet.</p>";

        return;
    }

    for (const task of tasks) {

        const taskElement =
            document.createElement("div");

        taskElement.classList.add("task");

        taskElement.innerHTML = `
            <h3>${task.name}</h3>

            <p>Due: ${task.dueDate}</p>

            <p>Priority: ${task.priority}</p>

            <p>
                Status:
                ${task.completed
                    ? "Completed"
                    : "Active"}
            </p>

            ${
                !task.completed
                    ? `<button class="complete-button">
                           Complete
                       </button>`
                    : ""
            }

            <button class="delete-button">
                Delete
            </button>
        `;

        const completeButton =
            taskElement.querySelector(
                ".complete-button"
            );

        if (completeButton) {

            completeButton.addEventListener(
                "click",
                () => completeTask(task.id)
            );
        }

        const deleteButton =
            taskElement.querySelector(
                ".delete-button"
            );

        deleteButton.addEventListener(
            "click",
            () => deleteTask(task.id)
        );

        taskList.appendChild(taskElement);
    }
}

async function completeTask(id) {

    try {

        const response = await fetch(
            `/api/tasks/${id}`,
            {
                method: "PUT"
            }
        );

        if (!response.ok) {
            throw new Error(
                "Failed to complete task"
            );
        }

        await loadTasks();

    } catch (error) {

        console.error(
            "Error completing task:",
            error
        );
    }
}

async function deleteTask(id) {

    try {

        const response = await fetch(
            `/api/tasks/${id}`,
            {
                method: "DELETE"
            }
        );

        if (!response.ok) {
            throw new Error(
                "Failed to delete task"
            );
        }

        await loadTasks();

    } catch (error) {

        console.error(
            "Error deleting task:",
            error
        );
    }
}

loadTasks();