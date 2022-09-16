package path.e04_configuration_behavior;

import com.mx.accessors.AccessorConfiguration;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.AccessorResponseStatus;
import com.mx.accessors.transfer.TransferBaseAccessor;
import com.mx.models.MdxList;
import com.mx.models.transfer.Transfer;
import com.mx.models.transfer.options.TransferListOptions;

public class TransferAccessor extends TransferBaseAccessor {

  public TransferAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<MdxList<Transfer>> list(TransferListOptions options) {

    MdxList<Transfer> transfers = new MdxList<>();
    return new AccessorResponse<MdxList<Transfer>>()
        .withResult(transfers)
        .withStatus(AccessorResponseStatus.OK);
  }

}
