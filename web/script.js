const taskList = document.getElementById("task-list");

const taskForm = document.getElementById("task-form");
const taskNameInput = document.getElementById("task-name");
const dueDateInput = document.getElementById("due-date");
const priorityInput = document.getElementById("priority");

const filterButtons =
    document.querySelectorAll("[data-filter]");

let allTasks = [];
let currentFilter = "all";


// Add Task form
taskForm.addEventListener(
    "submit",
    handleAddTask
);


// Filter buttons
for (const button of filterButtons) {

    button.addEventListener(
        "click",
        () => {

            currentFilter =
                button.dataset.filter;

            applyCurrentFilter();
        }
    );
}


// CREATE TASK
async function handleAddTask(event) {

    event.preventDefault();

    const newTask = {
        name: taskNameInput.value,
        dueDate: dueDateInput.value,
        priority: priorityInput.value
    };

    try {

        const response = await fetch(
            "/api/tasks",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(newTask)
            }
        );

        if (!response.ok) {

            throw new Error(
                "Failed to create task"
            );
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


// LOAD TASKS
async function loadTasks() {

    try {

        const response =
            await fetch("/api/tasks");

        if (!response.ok) {

            throw new Error(
                "Failed to load tasks"
            );
        }

        allTasks =
            await response.json();

        applyCurrentFilter();

    } catch (error) {

        console.error(
            "Error loading tasks:",
            error
        );

        taskList.innerHTML =
            "<p>Could not load tasks.</p>";
    }
}


// FILTER TASKS
function applyCurrentFilter() {

    let filteredTasks;

    if (currentFilter === "active") {

        filteredTasks =
            allTasks.filter(
                task => !task.completed
            );

    } else if (
        currentFilter === "completed"
    ) {

        filteredTasks =
            allTasks.filter(
                task => task.completed
            );

    } else if (
        currentFilter === "overdue"
    ) {

        const today = new Date();

        today.setHours(
            0,
            0,
            0,
            0
        );

        filteredTasks =
            allTasks.filter(
                task => {

                    const dueDate =
                        new Date(
                            task.dueDate
                            + "T00:00:00"
                        );

                    return (
                        !task.completed
                        && dueDate < today
                    );
                }
            );

    } else {

        filteredTasks = allTasks;
    }

    renderTasks(filteredTasks);
}


// DISPLAY TASKS
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

        taskElement.classList.add(
            "task"
        );

        taskElement.innerHTML = `
            <h3>
                ${task.name}
            </h3>

            <p>
                Due: ${task.dueDate}
            </p>

            <p>
                Priority: ${task.priority}
            </p>

            <p>
                Status:
                ${
                    task.completed
                        ? "Completed"
                        : "Active"
                }
            </p>

            ${
                !task.completed
                    ? `
                        <button
                            class="complete-button"
                        >
                            Complete
                        </button>
                    `
                    : ""
            }

            <button
                class="delete-button"
            >
                Delete
            </button>
        `;


        // Complete button
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


        // Delete button
        const deleteButton =
            taskElement.querySelector(
                ".delete-button"
            );

        deleteButton.addEventListener(
            "click",
            () => deleteTask(task.id)
        );


        taskList.appendChild(
            taskElement
        );
    }
}


// COMPLETE TASK
async function completeTask(id) {

    try {

        const response =
            await fetch(
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


// DELETE TASK
async function deleteTask(id) {

    try {

        const response =
            await fetch(
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


// Load tasks when page first opens
loadTasks();