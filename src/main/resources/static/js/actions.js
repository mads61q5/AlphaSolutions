// Dropdown functionality
document.addEventListener('DOMContentLoaded', function() {
    // Close all dropdowns when clicking outside
    document.addEventListener('click', function(event) {
        if (!event.target.matches('.dropdown-btn')) {
            const dropdowns = document.getElementsByClassName('dropdown-content');
            for (const dropdown of dropdowns) {
                if (dropdown.parentElement.classList.contains('show')) {
                    dropdown.parentElement.classList.remove('show');
                }
            }
        }
    });

    // Toggle dropdown on button click
    const dropdownBtns = document.querySelectorAll('.dropdown-btn');
    dropdownBtns.forEach(btn => {
        btn.addEventListener('click', function(event) {
            event.stopPropagation();
            const dropdown = this.parentElement;
            // Close all other dropdowns
            document.querySelectorAll('.dropdown.show').forEach(d => {
                if (d !== dropdown) d.classList.remove('show');
            });
            // Toggle current dropdown
            dropdown.classList.toggle('show');
        });
    });

    // Keyboard navigation
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            document.querySelectorAll('.dropdown.show').forEach(d => d.classList.remove('show'));
            closeModal();
        }
    });
});

// Modal functionality
function showModal(taskId) {
    const overlay = document.querySelector('.modal-overlay');
    const modal = document.querySelector('.modal');
    const form = modal.querySelector('form');
    
    // Update form action with correct task ID
    if (form) {
        const action = form.getAttribute('data-action-template').replace('TASK_ID', taskId);
        form.setAttribute('action', action);
    }
    
    overlay.classList.add('show');
    modal.classList.add('show');
}

function closeModal() {
    const overlay = document.querySelector('.modal-overlay');
    const modal = document.querySelector('.modal');
    
    overlay.classList.remove('show');
    modal.classList.remove('show');
}

// Add hours form submission
document.addEventListener('DOMContentLoaded', function() {
    const addHoursForm = document.querySelector('.add-hours-form');
    if (addHoursForm) {
        addHoursForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const formData = new FormData(this);
            
            fetch(this.action, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: formData
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    // Update task row time spent
                    const taskRow = document.querySelector(`tr[data-task-id="${data.taskId}"]`);
                    if (taskRow) {
                        taskRow.querySelector('.task-time-spent').textContent = data.newTimeSpent;
                    }
                    
                    // Update total time spent displays
                    if (data.totalTimeSpent !== undefined) {
                        const taskTotalDisplay = document.getElementById('taskTotalTimeSpentDisplay');
                        if (taskTotalDisplay) {
                            taskTotalDisplay.textContent = data.totalTimeSpent;
                        }
                        
                        const subProjectTimeSpentDisplay = document.getElementById('subProjectInfoTimeSpentDisplay');
                        if (subProjectTimeSpentDisplay) {
                            subProjectTimeSpentDisplay.textContent = data.totalTimeSpent;
                        }

                        const timeSummaryDisplay = document.getElementById('timeSummaryInBoxTotalTimeSpent');
                        if (timeSummaryDisplay) {
                            timeSummaryDisplay.textContent = data.totalTimeSpent + 'h';
                        }
                    }
                    
                    closeModal();
                } else {
                    console.error('Operation failed:', data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred while updating the hours. Please try again.');
            });
        });
    }
}); 