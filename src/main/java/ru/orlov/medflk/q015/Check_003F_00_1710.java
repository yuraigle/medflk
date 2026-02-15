package ru.orlov.medflk.q015;

import org.springframework.stereotype.Component;
import ru.orlov.medflk.jaxb.FlkErr;
import ru.orlov.medflk.jaxb.Pers;
import ru.orlov.medflk.jaxb.PersList;
import ru.orlov.medflk.jaxb.ZlList;

import java.util.List;

import static ru.orlov.medflk.Utils.getMdTypeFromFilename;

@Component
public class Check_003F_00_1710 extends AbstractCheck {

    @Override
    public String getErrorMessage() {
        return "Поле телефон должно отсутствовать для файлов отличных от диспансеризации";
    }

    @Override
    public List<FlkErr> check(ZlList zlList, PersList persList) {
        String typeMd = getMdTypeFromFilename(zlList.getZglv().getFilename());
        if (!List.of("H", "T", "C").contains(typeMd)) return List.of();

        for (Pers pers : persList.getPersList()) {
            if (pers.getTel() != null) {
                return List.of(new FlkErr(pers, pers.getTel()));
            }
        }

        return List.of();
    }
}
