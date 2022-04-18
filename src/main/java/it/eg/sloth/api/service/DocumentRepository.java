package it.eg.sloth.api.service;

import it.eg.sloth.api.model.db.DocumentPojo;
import it.eg.sloth.core.db.FilteredQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT = "Insert Into Document (Iddocument, Name, DocumentDate, Cost, Flagactive) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "Update Document set Iddocument = ?, Name = ?, DocumentDate = ?, Cost = ?, Flagactive = ? where Iddocument = ?";

    private static final String SQL_DELETE = "Delete From Document where idDocument = ?";

    private static final String SQL_SELECT = "Select *\n" +
            "From Document\n" +
            "/*W*/\n" +
            "Order By 1";

    public List<DocumentPojo> select(Integer idDocument) {
        return new FilteredQuery(jdbcTemplate, SQL_SELECT)
                .addBaseFilter("idDocument = ?", idDocument)
                .selectTable(DocumentPojo.class);
    }

    public int insert(DocumentPojo documentPojo) {
        return jdbcTemplate.update(SQL_INSERT,
                documentPojo.getIdDocument(),
                documentPojo.getName(),
                documentPojo.getDocumentDate(),
                documentPojo.getCost(),
                documentPojo.getFlagActive());
    }

    public int update(DocumentPojo documentPojo) {
        return jdbcTemplate.update(SQL_UPDATE,
                documentPojo.getIdDocument(),
                documentPojo.getName(),
                documentPojo.getDocumentDate(),
                documentPojo.getCost(),
                documentPojo.getFlagActive(),
                documentPojo.getIdDocument());
    }

    public int delete(Integer idDocument) {
        return jdbcTemplate.update(SQL_DELETE, idDocument);
    }

}
