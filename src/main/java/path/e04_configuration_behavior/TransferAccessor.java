package path.e04_configuration_behavior;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.model.mdx.accessor.transfer.TransferBaseAccessor;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.transfer.Transfer;
import com.mx.path.model.mdx.model.transfer.options.TransferListOptions;

public class TransferAccessor extends TransferBaseAccessor {

  public TransferAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<MdxList<Transfer>> list(TransferListOptions options) {

    MdxList<Transfer> transfers = new MdxList<>();
    return new AccessorResponse<MdxList<Transfer>>()
        .withResult(transfers)
        .withStatus(PathResponseStatus.OK);
  }

}
