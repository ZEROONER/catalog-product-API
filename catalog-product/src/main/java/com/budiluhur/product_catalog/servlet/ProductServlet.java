package com.budiluhur.product_catalog.servlet;

import com.budiluhur.product_catalog.exception.ProductNotFoundException;
import com.budiluhur.product_catalog.model.Product;
import com.budiluhur.product_catalog.repository.ProductRepository;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductRepository productRepository;

    @Override
    public void init() {

        productRepository = new ProductRepository();

        productRepository.addProduct(
            new Product("PRD-01", "Keyboard Mechanical", 450000.0)
        );
        productRepository.addProduct(
            new Product("PRD-02", "Mouse Wireless Silent", 175000.0)
        );
        productRepository.addProduct(
            new Product("PRD-03", "Monitor Gaming 24 Inch", 2100000.0)
        );
    }

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // Ambil query parameter "id"
        String id = request.getParameter("id");

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Katalog Produk Web</title>");
        out.println("</head>");
        out.println("<body>");

        if (id != null && !id.trim().isEmpty()) {
            tampilkanDetail(id.trim(), response, out);
        } else {
            tampilkanSemua(out);
        }

        out.println("</body>");
        out.println("</html>");
    }

    // Detail satu produk, atau error 404 jika tidak ditemukan
    private void tampilkanDetail(
        String id,
        HttpServletResponse response,
        PrintWriter out
    ) {
        try {
            Product p = productRepository.findById(id);

            out.println("<h2>=== DETAIL PRODUK ===</h2>");
            out.println("<table border='1' cellpadding='8'>");
            out.println("<tr><th>ID Produk</th><td>" + p.getId() + "</td></tr>");
            out.println("<tr><th>Nama Produk</th><td>" + p.getName() + "</td></tr>");
            out.println("<tr><th>Harga Satuan</th><td>Rp" + p.getPrice() + "</td></tr>");
            out.println("</table>");
            out.println("<p><a href='products'>&laquo; Kembali ke daftar produk</a></p>");

        } catch (ProductNotFoundException e) {

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            out.println(
                "<h3 style='color:red;'>Error 404: "
                + escapeHtml(e.getMessage())
                + "</h3>"
            );
            out.println("<p><a href='products'>&laquo; Kembali ke daftar produk</a></p>");
        }
    }

    // Daftar semua produk
    private void tampilkanSemua(PrintWriter out) {

        List<Product> products = productRepository.findAll();

        out.println("<h2>=== DAFTAR KATALOG PRODUK (WEB) ===</h2>");
        out.println("<table border='1' cellpadding='8'>");
        out.println(
            "<tr><th>ID Produk</th><th>Nama Produk</th>"
            + "<th>Harga Satuan</th></tr>"
        );

        for (Product p : products) {
            out.println("<tr>");
            out.println(
                "<td><a href='products?id=" + p.getId() + "'>"
                + p.getId() + "</a></td>"
            );
            out.println("<td>" + p.getName() + "</td>");
            out.println("<td>Rp" + p.getPrice() + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
    }

    // Mencegah HTML/script injection dari input pengguna
    private String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
