package de.davis.passwordmanager.sync.csv;

import android.content.Context;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.validators.RowFunctionValidator;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;

import de.davis.passwordmanager.R;
import de.davis.passwordmanager.database.SecureElementDatabase;
import de.davis.passwordmanager.security.element.SecureElement;
import de.davis.passwordmanager.security.element.SecureElementManager;
import de.davis.passwordmanager.security.element.password.PasswordDetails;
import de.davis.passwordmanager.sync.DataTransfer;
import de.davis.passwordmanager.sync.Result;

public class CsvTransfer extends DataTransfer {


    public CsvTransfer(Context context) {
        super(context);
    }

    @Override
    protected Result importElements(InputStream inputStream, String password) throws Exception {
        CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream))
                .withSkipLines(1)
                .withRowValidator(new RowFunctionValidator(s -> s.length == 5, getContext().getString(R.string.csv_row_number_error)))
                .withRowValidator(new RowFunctionValidator(s -> s.length == 5, getContext().getString(R.string.csv_row_number_error)))
                .build();

        String[] line;

        List<SecureElement> elements = SecureElementDatabase.getInstance()
                .getSecureElementDao()
                .getAllByType(SecureElement.TYPE_PASSWORD)
                .blockingGet();

        int existed = 0;
        while ((line = csvReader.readNext()) != null) {
            if(line[0].isEmpty() || line[3].isEmpty()) // name and password must not be empty
                continue;

            String title = line[0];
            if(elements.stream().anyMatch(element -> element.getTitle().equals(title))) {
                existed++;
                continue;
            }

            PasswordDetails details = new PasswordDetails(line[3], line[1], line[2]);
            SecureElementManager.getInstance().createElement(new SecureElement(details, title));
        }

        csvReader.close();

        if(existed != 0)
            return new Result.Error(getContext().getResources().getQuantityString(R.plurals.item_title_existed, existed, existed));

        return new Result.Success(TYPE_IMPORT);
    }

    @Override
    protected Result exportElements(OutputStream outputStream, String password) throws Exception {
        CSVWriter csvWriter = (CSVWriter) new CSVWriterBuilder(new OutputStreamWriter(outputStream))
                .build();

        List<SecureElement> elements = SecureElementDatabase.getInstance()
                .getSecureElementDao()
                .getAllByType(SecureElement.TYPE_PASSWORD)
                .blockingGet();

        csvWriter.writeNext(new String[]{"name", "url", "username", "password", "note"});


        csvWriter.writeAll(elements.stream().map(pwd -> new String[]{pwd.getTitle(),
                ((PasswordDetails)pwd.getDetail()).getOrigin(),
                ((PasswordDetails)pwd.getDetail()).getUsername(),
                ((PasswordDetails)pwd.getDetail()).getPassword(),
                null}).collect(Collectors.toList()));

        csvWriter.flush();
        csvWriter.close();

        return new Result.Success(TYPE_EXPORT);
    }
}
