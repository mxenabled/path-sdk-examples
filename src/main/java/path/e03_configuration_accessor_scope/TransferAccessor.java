package path.e03_configuration_accessor_scope;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.gateway.accessor.AccessorConfiguration;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.configuration.annotations.AccessorScope;
import com.mx.path.gateway.configuration.annotations.MaxScope;
import com.mx.path.model.mdx.accessor.transfer.TransferBaseAccessor;
import com.mx.path.model.mdx.model.MdxList;
import com.mx.path.model.mdx.model.transfer.Transfer;
import com.mx.path.model.mdx.model.transfer.options.TransferListOptions;

@MaxScope(AccessorScope.PROTOTYPE)
public class TransferAccessor extends TransferBaseAccessor {

  public TransferAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  @Override
  public final AccessorResponse<MdxList<Transfer>> list(TransferListOptions options) {
    MdxList<Transfer> transfers = new MdxList<>();
    return new AccessorResponse<MdxList<Transfer>>()
        .withResult(transfers)
        .withStatus(PathResponseStatus.OK)
        // Pass this object id back to caller
        .withHeader("accessorId", String.valueOf(System.identityHashCode(this)));
  }

}
