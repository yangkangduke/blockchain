package com.seeds.chain.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class GameItems extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b506040805160208101909152600081526200002c816200003e565b50620000383362000050565b62000213565b60026200004c828262000147565b5050565b600380546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e090600090a35050565b634e487b7160e01b600052604160045260246000fd5b600181811c90821680620000cd57607f821691505b602082108103620000ee57634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156200014257600081815260208120601f850160051c810160208610156200011d5750805b601f850160051c820191505b818110156200013e5782815560010162000129565b5050505b505050565b81516001600160401b03811115620001635762000163620000a2565b6200017b81620001748454620000b8565b84620000f4565b602080601f831160018114620001b357600084156200019a5750858301515b600019600386901b1c1916600185901b1785556200013e565b600085815260208120601f198616915b82811015620001e457888601518255948401946001909101908401620001c3565b5085821015620002035787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b611f6480620002236000396000f3fe608060405234801561001057600080fd5b506004361061010a5760003560e01c80636b20c454116100a2578063d81d0a1511610071578063d81d0a151461022f578063e985e9c514610242578063f242432a1461027e578063f2fde38b14610291578063f5298aca146102a457600080fd5b80636b20c454146101e6578063715018a6146101f95780638da5cb5b14610201578063a22cb4651461021c57600080fd5b8063162094c4116100de578063162094c41461018d5780632eb2c2d6146101a05780634e1273f4146101b3578063674d2788146101d357600080fd5b8062fdd58e1461010f57806301ffc9a7146101355780630e89341c14610158578063156e29f614610178575b600080fd5b61012261011d3660046113a1565b6102b7565b6040519081526020015b60405180910390f35b6101486101433660046113e1565b610350565b604051901515815260200161012c565b61016b610166366004611405565b6103a0565b60405161012c9190611464565b61018b610186366004611477565b610442565b005b61018b61019b366004611561565b61046a565b61018b6101ae36600461163d565b61048a565b6101c66101c13660046116e7565b6104d6565b60405161012c91906117e3565b6101226101e13660046117f6565b610600565b61018b6101f436600461184d565b610694565b61018b6106d7565b6003546040516001600160a01b03909116815260200161012c565b61018b61022a3660046118b7565b6106eb565b61018b61023d36600461184d565b6106fa565b6101486102503660046118f3565b6001600160a01b03918216600090815260016020908152604080832093909416825291909152205460ff1690565b61018b61028c366004611926565b61071d565b61018b61029f36600461198b565b610762565b61018b6102b2366004611477565b6107db565b60006001600160a01b0383166103275760405162461bcd60e51b815260206004820152602a60248201527f455243313135353a2061646472657373207a65726f206973206e6f742061207660448201526930b634b21037bbb732b960b11b60648201526084015b60405180910390fd5b506000818152602081815260408083206001600160a01b03861684529091529020545b92915050565b60006001600160e01b03198216636cdb3d1360e11b148061038157506001600160e01b031982166303a24d0760e21b145b8061034a57506301ffc9a760e01b6001600160e01b031983161461034a565b60008181526005602052604090208054606091906103bd906119a6565b80601f01602080910402602001604051908101604052809291908181526020018280546103e9906119a6565b80156104365780601f1061040b57610100808354040283529160200191610436565b820191906000526020600020905b81548152906001019060200180831161041957829003601f168201915b50505050509050919050565b61044a61081e565b61046583838360405180602001604052806000815250610878565b505050565b61047261081e565b60008281526005602052604090206104658282611a26565b6001600160a01b0385163314806104a657506104a68533610250565b6104c25760405162461bcd60e51b815260040161031e90611ae6565b6104cf8585858585610952565b5050505050565b6060815183511461053b5760405162461bcd60e51b815260206004820152602960248201527f455243313135353a206163636f756e747320616e6420696473206c656e677468604482015268040dad2e6dac2e8c6d60bb1b606482015260840161031e565b6000835167ffffffffffffffff811115610557576105576114aa565b604051908082528060200260200182016040528015610580578160200160208202803683370190505b50905060005b84518110156105f8576105cb8582815181106105a4576105a4611b35565b60200260200101518583815181106105be576105be611b35565b60200260200101516102b7565b8282815181106105dd576105dd611b35565b60209081029190910101526105f181611b61565b9050610586565b509392505050565b600061060a61081e565b610618600480546001019055565b600061062360045490565b905061064085828660405180602001604052806000815250610878565b60008181526005602052604090206106588482611a26565b506040518181527f94242c431036b9ba6723a138d4b275a5b38e13a95ef66227a45df427c0f843f39060200160405180910390a1949350505050565b6001600160a01b0383163314806106b057506106b08333610250565b6106cc5760405162461bcd60e51b815260040161031e90611ae6565b610465838383610aee565b6106df61081e565b6106e96000610c79565b565b6106f6338383610ccb565b5050565b61070261081e565b61046583838360405180602001604052806000815250610dab565b6001600160a01b03851633148061073957506107398533610250565b6107555760405162461bcd60e51b815260040161031e90611ae6565b6104cf8585858585610ef6565b61076a61081e565b6001600160a01b0381166107cf5760405162461bcd60e51b815260206004820152602660248201527f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160448201526564647265737360d01b606482015260840161031e565b6107d881610c79565b50565b6001600160a01b0383163314806107f757506107f78333610250565b6108135760405162461bcd60e51b815260040161031e90611ae6565b610465838383611020565b6003546001600160a01b031633146106e95760405162461bcd60e51b815260206004820181905260248201527f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e6572604482015260640161031e565b6001600160a01b03841661089e5760405162461bcd60e51b815260040161031e90611b7a565b3360006108aa85611124565b905060006108b785611124565b90506000868152602081815260408083206001600160a01b038b168452909152812080548792906108e9908490611bbb565b909155505060408051878152602081018790526001600160a01b03808a1692600092918716917fc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62910160405180910390a46109498360008989898961116f565b50505050505050565b81518351146109735760405162461bcd60e51b815260040161031e90611bce565b6001600160a01b0384166109995760405162461bcd60e51b815260040161031e90611c16565b3360005b8451811015610a805760008582815181106109ba576109ba611b35565b6020026020010151905060008583815181106109d8576109d8611b35565b602090810291909101810151600084815280835260408082206001600160a01b038e168352909352919091205490915081811015610a285760405162461bcd60e51b815260040161031e90611c5b565b6000838152602081815260408083206001600160a01b038e8116855292528083208585039055908b16825281208054849290610a65908490611bbb565b9250508190555050505080610a7990611b61565b905061099d565b50846001600160a01b0316866001600160a01b0316826001600160a01b03167f4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb8787604051610ad0929190611ca5565b60405180910390a4610ae68187878787876112ca565b505050505050565b6001600160a01b038316610b145760405162461bcd60e51b815260040161031e90611cd3565b8051825114610b355760405162461bcd60e51b815260040161031e90611bce565b604080516020810190915260009081905233905b8351811015610c0b576000848281518110610b6657610b66611b35565b602002602001015190506000848381518110610b8457610b84611b35565b602090810291909101810151600084815280835260408082206001600160a01b038c168352909352919091205490915081811015610bd45760405162461bcd60e51b815260040161031e90611d16565b6000928352602083815260408085206001600160a01b038b1686529091529092209103905580610c0381611b61565b915050610b49565b5060006001600160a01b0316846001600160a01b0316826001600160a01b03167f4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb8686604051610c5c929190611ca5565b60405180910390a460408051602081019091526000905250505050565b600380546001600160a01b038381166001600160a01b0319831681179093556040519116919082907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e090600090a35050565b816001600160a01b0316836001600160a01b031603610d3e5760405162461bcd60e51b815260206004820152602960248201527f455243313135353a2073657474696e6720617070726f76616c20737461747573604482015268103337b91039b2b63360b91b606482015260840161031e565b6001600160a01b03838116600081815260016020908152604080832094871680845294825291829020805460ff191686151590811790915591519182527f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31910160405180910390a3505050565b6001600160a01b038416610dd15760405162461bcd60e51b815260040161031e90611b7a565b8151835114610df25760405162461bcd60e51b815260040161031e90611bce565b3360005b8451811015610e8e57838181518110610e1157610e11611b35565b6020026020010151600080878481518110610e2e57610e2e611b35565b602002602001015181526020019081526020016000206000886001600160a01b03166001600160a01b031681526020019081526020016000206000828254610e769190611bbb565b90915550819050610e8681611b61565b915050610df6565b50846001600160a01b031660006001600160a01b0316826001600160a01b03167f4a39dc06d4c0dbc64b70af90fd698a233a518aa5d07e595d983b8c0526c8f7fb8787604051610edf929190611ca5565b60405180910390a46104cf816000878787876112ca565b6001600160a01b038416610f1c5760405162461bcd60e51b815260040161031e90611c16565b336000610f2885611124565b90506000610f3585611124565b90506000868152602081815260408083206001600160a01b038c16845290915290205485811015610f785760405162461bcd60e51b815260040161031e90611c5b565b6000878152602081815260408083206001600160a01b038d8116855292528083208985039055908a16825281208054889290610fb5908490611bbb565b909155505060408051888152602081018890526001600160a01b03808b16928c821692918816917fc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62910160405180910390a4611015848a8a8a8a8a61116f565b505050505050505050565b6001600160a01b0383166110465760405162461bcd60e51b815260040161031e90611cd3565b33600061105284611124565b9050600061105f84611124565b60408051602080820183526000918290528882528181528282206001600160a01b038b16835290522054909150848110156110ac5760405162461bcd60e51b815260040161031e90611d16565b6000868152602081815260408083206001600160a01b038b81168086529184528285208a8703905582518b81529384018a90529092908816917fc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62910160405180910390a4604080516020810190915260009052610949565b6040805160018082528183019092526060916000919060208083019080368337019050509050828160008151811061115e5761115e611b35565b602090810291909101015292915050565b6001600160a01b0384163b15610ae65760405163f23a6e6160e01b81526001600160a01b0385169063f23a6e61906111b39089908990889088908890600401611d5a565b6020604051808303816000875af19250505080156111ee575060408051601f3d908101601f191682019092526111eb91810190611d9f565b60015b61129a576111fa611dbc565b806308c379a003611233575061120e611dd8565b806112195750611235565b8060405162461bcd60e51b815260040161031e9190611464565b505b60405162461bcd60e51b815260206004820152603460248201527f455243313135353a207472616e7366657220746f206e6f6e20455243313135356044820152732932b1b2b4bb32b91034b6b83632b6b2b73a32b960611b606482015260840161031e565b6001600160e01b0319811663f23a6e6160e01b146109495760405162461bcd60e51b815260040161031e90611e62565b6001600160a01b0384163b15610ae65760405163bc197c8160e01b81526001600160a01b0385169063bc197c819061130e9089908990889088908890600401611eaa565b6020604051808303816000875af1925050508015611349575060408051601f3d908101601f1916820190925261134691810190611d9f565b60015b611355576111fa611dbc565b6001600160e01b0319811663bc197c8160e01b146109495760405162461bcd60e51b815260040161031e90611e62565b80356001600160a01b038116811461139c57600080fd5b919050565b600080604083850312156113b457600080fd5b6113bd83611385565b946020939093013593505050565b6001600160e01b0319811681146107d857600080fd5b6000602082840312156113f357600080fd5b81356113fe816113cb565b9392505050565b60006020828403121561141757600080fd5b5035919050565b6000815180845260005b8181101561144457602081850181015186830182015201611428565b506000602082860101526020601f19601f83011685010191505092915050565b6020815260006113fe602083018461141e565b60008060006060848603121561148c57600080fd5b61149584611385565b95602085013595506040909401359392505050565b634e487b7160e01b600052604160045260246000fd5b601f8201601f1916810167ffffffffffffffff811182821017156114e6576114e66114aa565b6040525050565b600082601f8301126114fe57600080fd5b813567ffffffffffffffff811115611518576115186114aa565b60405161152f601f8301601f1916602001826114c0565b81815284602083860101111561154457600080fd5b816020850160208301376000918101602001919091529392505050565b6000806040838503121561157457600080fd5b82359150602083013567ffffffffffffffff81111561159257600080fd5b61159e858286016114ed565b9150509250929050565b600067ffffffffffffffff8211156115c2576115c26114aa565b5060051b60200190565b600082601f8301126115dd57600080fd5b813560206115ea826115a8565b6040516115f782826114c0565b83815260059390931b850182019282810191508684111561161757600080fd5b8286015b84811015611632578035835291830191830161161b565b509695505050505050565b600080600080600060a0868803121561165557600080fd5b61165e86611385565b945061166c60208701611385565b9350604086013567ffffffffffffffff8082111561168957600080fd5b61169589838a016115cc565b945060608801359150808211156116ab57600080fd5b6116b789838a016115cc565b935060808801359150808211156116cd57600080fd5b506116da888289016114ed565b9150509295509295909350565b600080604083850312156116fa57600080fd5b823567ffffffffffffffff8082111561171257600080fd5b818501915085601f83011261172657600080fd5b81356020611733826115a8565b60405161174082826114c0565b83815260059390931b850182019282810191508984111561176057600080fd5b948201945b838610156117855761177686611385565b82529482019490820190611765565b9650508601359250508082111561179b57600080fd5b5061159e858286016115cc565b600081518084526020808501945080840160005b838110156117d8578151875295820195908201906001016117bc565b509495945050505050565b6020815260006113fe60208301846117a8565b60008060006060848603121561180b57600080fd5b61181484611385565b925060208401359150604084013567ffffffffffffffff81111561183757600080fd5b611843868287016114ed565b9150509250925092565b60008060006060848603121561186257600080fd5b61186b84611385565b9250602084013567ffffffffffffffff8082111561188857600080fd5b611894878388016115cc565b935060408601359150808211156118aa57600080fd5b50611843868287016115cc565b600080604083850312156118ca57600080fd5b6118d383611385565b9150602083013580151581146118e857600080fd5b809150509250929050565b6000806040838503121561190657600080fd5b61190f83611385565b915061191d60208401611385565b90509250929050565b600080600080600060a0868803121561193e57600080fd5b61194786611385565b945061195560208701611385565b93506040860135925060608601359150608086013567ffffffffffffffff81111561197f57600080fd5b6116da888289016114ed565b60006020828403121561199d57600080fd5b6113fe82611385565b600181811c908216806119ba57607f821691505b6020821081036119da57634e487b7160e01b600052602260045260246000fd5b50919050565b601f82111561046557600081815260208120601f850160051c81016020861015611a075750805b601f850160051c820191505b81811015610ae657828155600101611a13565b815167ffffffffffffffff811115611a4057611a406114aa565b611a5481611a4e84546119a6565b846119e0565b602080601f831160018114611a895760008415611a715750858301515b600019600386901b1c1916600185901b178555610ae6565b600085815260208120601f198616915b82811015611ab857888601518255948401946001909101908401611a99565b5085821015611ad65787850151600019600388901b60f8161c191681555b5050505050600190811b01905550565b6020808252602f908201527f455243313135353a2063616c6c6572206973206e6f7420746f6b656e206f776e60408201526e195c881b9bdc88185c1c1c9bdd9959608a1b606082015260800190565b634e487b7160e01b600052603260045260246000fd5b634e487b7160e01b600052601160045260246000fd5b600060018201611b7357611b73611b4b565b5060010190565b60208082526021908201527f455243313135353a206d696e7420746f20746865207a65726f206164647265736040820152607360f81b606082015260800190565b8082018082111561034a5761034a611b4b565b60208082526028908201527f455243313135353a2069647320616e6420616d6f756e7473206c656e677468206040820152670dad2e6dac2e8c6d60c31b606082015260800190565b60208082526025908201527f455243313135353a207472616e7366657220746f20746865207a65726f206164604082015264647265737360d81b606082015260800190565b6020808252602a908201527f455243313135353a20696e73756666696369656e742062616c616e636520666f60408201526939103a3930b739b332b960b11b606082015260800190565b604081526000611cb860408301856117a8565b8281036020840152611cca81856117a8565b95945050505050565b60208082526023908201527f455243313135353a206275726e2066726f6d20746865207a65726f206164647260408201526265737360e81b606082015260800190565b60208082526024908201527f455243313135353a206275726e20616d6f756e7420657863656564732062616c604082015263616e636560e01b606082015260800190565b6001600160a01b03868116825285166020820152604081018490526060810183905260a060808201819052600090611d949083018461141e565b979650505050505050565b600060208284031215611db157600080fd5b81516113fe816113cb565b600060033d1115611dd55760046000803e5060005160e01c5b90565b600060443d1015611de65790565b6040516003193d81016004833e81513d67ffffffffffffffff8160248401118184111715611e1657505050505090565b8285019150815181811115611e2e5750505050505090565b843d8701016020828501011115611e485750505050505090565b611e57602082860101876114c0565b509095945050505050565b60208082526028908201527f455243313135353a204552433131353552656365697665722072656a656374656040820152676420746f6b656e7360c01b606082015260800190565b6001600160a01b0386811682528516602082015260a060408201819052600090611ed6908301866117a8565b8281036060840152611ee881866117a8565b90508281036080840152611efc818561141e565b9897505050505050505056fea26469706673582212208987f310ce7591de9d05042884095dafe564fc9280cd22fa91c16730cb8acddb64736f6c637828302e382e31372d646576656c6f702e323032322e382e31312b636f6d6d69742e33633061373335350059";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BALANCEOFBATCH = "balanceOfBatch";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_BURNBATCH = "burnBatch";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_MINTBATCH = "mintBatch";

    public static final String FUNC_MINTNEWNFT = "mintNewNft";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SAFEBATCHTRANSFERFROM = "safeBatchTransferFrom";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_SETTOKENURI = "setTokenURI";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_URI = "uri";

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bool>() {}));
    ;

    public static final Event MINTEVENT_EVENT = new Event("MintEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSFERBATCH_EVENT = new Event("TransferBatch",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
    ;

    public static final Event TRANSFERSINGLE_EVENT = new Event("TransferSingle",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event URI_EVENT = new Event("URI",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>(true) {}));
    ;

    @Deprecated
    protected GameItems(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected GameItems(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected GameItems(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected GameItems(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ApprovalForAllEventResponse> getApprovalForAllEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt);
        ArrayList<ApprovalForAllEventResponse> responses = new ArrayList<ApprovalForAllEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ApprovalForAllEventResponse>() {
            @Override
            public ApprovalForAllEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVALFORALL_EVENT, log);
                ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
                typedResponse.log = log;
                typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.operator = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT));
        return approvalForAllEventFlowable(filter);
    }

    public List<MintEventEventResponse> getMintEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(MINTEVENT_EVENT, transactionReceipt);
        ArrayList<MintEventEventResponse> responses = new ArrayList<MintEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MintEventEventResponse typedResponse = new MintEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newItemId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MintEventEventResponse> mintEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, MintEventEventResponse>() {
            @Override
            public MintEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(MINTEVENT_EVENT, log);
                MintEventEventResponse typedResponse = new MintEventEventResponse();
                typedResponse.log = log;
                typedResponse.newItemId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MintEventEventResponse> mintEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MINTEVENT_EVENT));
        return mintEventEventFlowable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public List<TransferBatchEventResponse> getTransferBatchEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFERBATCH_EVENT, transactionReceipt);
        ArrayList<TransferBatchEventResponse> responses = new ArrayList<TransferBatchEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferBatchEventResponse typedResponse = new TransferBatchEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.from = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.ids = (List<BigInteger>) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.values = (List<BigInteger>) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferBatchEventResponse> transferBatchEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferBatchEventResponse>() {
            @Override
            public TransferBatchEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFERBATCH_EVENT, log);
                TransferBatchEventResponse typedResponse = new TransferBatchEventResponse();
                typedResponse.log = log;
                typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.from = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.ids = (List<BigInteger>) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.values = (List<BigInteger>) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferBatchEventResponse> transferBatchEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFERBATCH_EVENT));
        return transferBatchEventFlowable(filter);
    }

    public List<TransferSingleEventResponse> getTransferSingleEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFERSINGLE_EVENT, transactionReceipt);
        ArrayList<TransferSingleEventResponse> responses = new ArrayList<TransferSingleEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferSingleEventResponse typedResponse = new TransferSingleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.from = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferSingleEventResponse> transferSingleEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferSingleEventResponse>() {
            @Override
            public TransferSingleEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFERSINGLE_EVENT, log);
                TransferSingleEventResponse typedResponse = new TransferSingleEventResponse();
                typedResponse.log = log;
                typedResponse.operator = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.from = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferSingleEventResponse> transferSingleEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFERSINGLE_EVENT));
        return transferSingleEventFlowable(filter);
    }

    public List<URIEventResponse> getURIEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(URI_EVENT, transactionReceipt);
        ArrayList<URIEventResponse> responses = new ArrayList<URIEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            URIEventResponse typedResponse = new URIEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.value = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<URIEventResponse> uRIEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, URIEventResponse>() {
            @Override
            public URIEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(URI_EVENT, log);
                URIEventResponse typedResponse = new URIEventResponse();
                typedResponse.log = log;
                typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.value = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<URIEventResponse> uRIEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(URI_EVENT));
        return uRIEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account, BigInteger id) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account),
                        new org.web3j.abi.datatypes.generated.Uint256(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> balanceOfBatch(List<String> accounts, List<BigInteger> ids) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCEOFBATCH,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                                org.web3j.abi.datatypes.Address.class,
                                org.web3j.abi.Utils.typeMap(accounts, org.web3j.abi.datatypes.Address.class)),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(ids, org.web3j.abi.datatypes.generated.Uint256.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> burn(String account, BigInteger id, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BURN,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account),
                        new org.web3j.abi.datatypes.generated.Uint256(id),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> burnBatch(String account, List<BigInteger> ids, List<BigInteger> values) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BURNBATCH,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(ids, org.web3j.abi.datatypes.generated.Uint256.class)),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(values, org.web3j.abi.datatypes.generated.Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> isApprovedForAll(String account, String operator) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISAPPROVEDFORALL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account),
                        new org.web3j.abi.datatypes.Address(160, operator)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> mint(String account, BigInteger id, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_MINT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account),
                        new org.web3j.abi.datatypes.generated.Uint256(id),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> mintBatch(String to, List<BigInteger> ids, List<BigInteger> amounts) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_MINTBATCH,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(ids, org.web3j.abi.datatypes.generated.Uint256.class)),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(amounts, org.web3j.abi.datatypes.generated.Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> mintNewNft(String account, BigInteger amount, String tokenURI) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_MINTNEWNFT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account),
                        new org.web3j.abi.datatypes.generated.Uint256(amount),
                        new org.web3j.abi.datatypes.Utf8String(tokenURI)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeBatchTransferFrom(String from, String to, List<BigInteger> ids, List<BigInteger> amounts, byte[] data) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SAFEBATCHTRANSFERFROM,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from),
                        new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(ids, org.web3j.abi.datatypes.generated.Uint256.class)),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(amounts, org.web3j.abi.datatypes.generated.Uint256.class)),
                        new org.web3j.abi.datatypes.DynamicBytes(data)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> safeTransferFrom(String from, String to, BigInteger id, BigInteger amount, byte[] data) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SAFETRANSFERFROM,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from),
                        new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(id),
                        new org.web3j.abi.datatypes.generated.Uint256(amount),
                        new org.web3j.abi.datatypes.DynamicBytes(data)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setApprovalForAll(String operator, Boolean approved) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETAPPROVALFORALL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, operator),
                        new org.web3j.abi.datatypes.Bool(approved)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setTokenURI(BigInteger tokenId, String newuri) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETTOKENURI,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId),
                        new org.web3j.abi.datatypes.Utf8String(newuri)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SUPPORTSINTERFACE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> uri(BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_URI,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(tokenId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static GameItems load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new GameItems(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static GameItems load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new GameItems(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static GameItems load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new GameItems(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static GameItems load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new GameItems(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<GameItems> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(GameItems.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<GameItems> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(GameItems.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<GameItems> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(GameItems.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<GameItems> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(GameItems.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ApprovalForAllEventResponse extends BaseEventResponse {
        public String account;

        public String operator;

        public Boolean approved;
    }

    public static class MintEventEventResponse extends BaseEventResponse {
        public BigInteger newItemId;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class TransferBatchEventResponse extends BaseEventResponse {
        public String operator;

        public String from;

        public String to;

        public List<BigInteger> ids;

        public List<BigInteger> values;
    }

    public static class TransferSingleEventResponse extends BaseEventResponse {
        public String operator;

        public String from;

        public String to;

        public BigInteger id;

        public BigInteger value;
    }

    public static class URIEventResponse extends BaseEventResponse {
        public BigInteger id;

        public String value;
    }
}
