package br.com.myteams.controller.home;

import br.com.myteams.dao.CategoriaDAO;
import br.com.myteams.dao.ProdutoDAO;
import br.com.myteams.model.categoria.Categoria;
import br.com.myteams.model.produto.Produto;
import br.com.myteams.util.CustomConnection;
import br.com.myteams.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/home")
public class HomeController extends HttpServlet
{

    /**
     * Recupera os produtos disponiveis no sistema e mostra na pagina inicial
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        CustomConnection connection = null;
        try
        {
            connection = DBUtil.getConnection();
            ProdutoDAO produtoDAO = new ProdutoDAO(connection);

            Categoria categoria = null;

            String categoriaId = req.getParameter("categoria");

            if (categoriaId != null && !categoriaId.isEmpty())
            {
                categoria = new CategoriaDAO(connection).procuraPorId(Long.parseLong(categoriaId));
            }

            List<Produto> produtos = produtoDAO.listaTodosComFiltro(categoria);

            req.setAttribute("produtos", produtos);
            req.setAttribute("categorias", new CategoriaDAO(connection).listaTodosQuePossuiProdutosAssociadosOrdenada());
            req.setAttribute("produtoMaisVendido", new ProdutoDAO(connection).buscaProdutoMaisVendido());

        }
        catch (SQLException e)
        {
            req.setAttribute("exception", e);

        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        req.getRequestDispatcher("/WEB-INF/produtos.jsp").forward(req, resp);

    }

}
