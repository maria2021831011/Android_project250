
const taskInput = document.getElementById("taskInput");
const addTaskButton = document.getElementById("addTaskButton");
const taskList = document.getElementById("taskList");


addTaskButton.addEventListener("click", addTask);

function addTask() {
    const task = taskInput.value.trim();

    if (task === "") {
        alert("Please enter a task!hey maria");
        return;
    }

    const taskItem = document.createElement("li");
    taskItem.className = "task-item";

   
    const taskText = document.createElement("span");
    taskText.textContent = task;

   
    const deleteButton = document.createElement("button");
    deleteButton.textContent = "Delete";
    deleteButton.addEventListener("click", () => {
        taskList.removeChild(taskItem);
    });

    
    taskItem.appendChild(taskText);
    taskItem.appendChild(deleteButton);
    taskList.appendChild(taskItem); 
    taskInput.value = "";
}
