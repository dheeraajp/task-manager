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

            <p>
                Due: ${task.dueDate}
            </p>

            <p>
                Priority: ${task.priority}
            </p>

            <p>
                Status:
                ${task.completed
                    ? "Completed"
                    : "Active"}
            </p>
        `;

        taskList.appendChild(
            taskElement
        );
    }
}

loadTasks();