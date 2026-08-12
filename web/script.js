const taskList = document.getElementById("task-list");

async function loadTasks() {
    try {
        const response = await fetch("/api/tasks");

        if (!response.ok) {
            throw new Error("Failed to load tasks");
        }

        const tasks = await response.json();

        renderTasks(tasks);

    } catch (error) {
        console.error("Error loading tasks:", error);

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
                ${task.completed ? "Completed" : "Active"}
            </p>
        `;

        taskList.appendChild(taskElement);
    }
}

loadTasks();