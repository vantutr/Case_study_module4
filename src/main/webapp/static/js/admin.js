document.addEventListener('DOMContentLoaded', function() {
    // Logic bật/tắt sidebar
    const menuToggle = document.getElementById("menu-toggle");
    if (menuToggle) {
        menuToggle.addEventListener("click", function(e) {
            e.preventDefault();
            document.getElementById("wrapper").classList.toggle("toggled");
        });
    }

    // --- LOGIC CHO MODAL SỬA DANH MỤC ---
    const editModal = document.getElementById('editCategoryModal');
    if (editModal) {
        // Lắng nghe sự kiện click trên TẤT CẢ các nút có class .btn-edit
        document.querySelectorAll('.btn-edit').forEach(button => {
            button.addEventListener('click', function() {
                // Lấy ID từ thuộc tính data-id của nút vừa được nhấn
                const categoryId = this.getAttribute('data-id');

                // Gọi API để lấy dữ liệu danh mục
                fetch(`/admin/categories/${categoryId}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Không tìm thấy danh mục');
                        }
                        return response.json();
                    })
                    .then(data => {
                        // Điền dữ liệu nhận được vào các trường trong form của modal Sửa
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