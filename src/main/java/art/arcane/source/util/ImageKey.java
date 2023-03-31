package art.arcane.source.util;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageKey {
    private String key;
    private SourceIO.ImageChannel channel;
}
