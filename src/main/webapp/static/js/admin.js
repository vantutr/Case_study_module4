document.addEventListener('DOMContentLoaded', function() {
    // Logic bật/tắt sidebar
    const menuToggle = document.getElementById("menu-toggle");
    const wrapper = document.getElementById("wrapper");
    if (menuToggle && wrapper) {
        menuToggle.addEventListener("click", function(e) {
            e.preventDefault();
            wrapper.classList.toggle("toggled");
        });
    }

    // Logic xử lý cho nút Sửa danh mục
    const editButtons = document.querySelectorAll('.btn-edit');
    const editModal = document.getElementById('editCategoryModal');

    if (editButtons && editModal) {
        editButtons.forEach(button => {
            button.addEventListener('click', function() {
                const categoryId = this.getAttribute('data-id');

                // Gọi API để lấy dữ liệu danh mục
                fetch(`/admin/categories/${categoryId}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        // Điền dữ liệu vào các trường trong modal Sửa
                        editModal.querySelector('#editCategoryId').value = data.id;
                        editModal.querySelector('#categoryNameEdit').value = data.name;
                        editModal.querySelector('#categoryDescriptionEdit').value = data.description || '';
                        editModal.querySelector('#parentCategoryEdit').value = data.parentId;
                    })
                    .catch(error => {
                        console.error('Lỗi khi lấy dữ liệu danh mục:', error);
                        alert('Không thể tải dữ liệu để sửa. Vui lòng thử lại.');
                    });
            });
        });
    }
});