const { ethers } = require("ethers");

const ERC721ABI = require(`./deployments/abi/ERC721.json`)
const ERC721Addr = require(`./deployments/dev/ERC721.json`)


function getFunctionID() {
    let transferTopic = ethers.utils.keccak256(
    ethers.utils.toUtf8Bytes("Transfer(address,address,uint256)"));
    console.log("transferTopic:" + transferTopic)
    let id = ethers.utils.id("Transfer(address,address,uint256)")
    console.log("Transfer:" + id);
}

async function parseTransferEvent(event) {
    const TransferEvent = new ethers.utils.Interface(["event Transfer(address indexed from,address indexed to,uint256 value)"]);
    let decodedData = TransferEvent.parseLog(event);
    console.log("from:" + decodedData.args.from);
    console.log("to:" + decodedData.args.to);
    console.log("value:" + decodedData.args.value.toString());
}

async function main() {
    let provider = new ethers.providers.WebSocketProvider('wss://eth-goerli.g.alchemy.com/v2/TEeAfIGvdAudHKz9qRZ2ctuNOpozkoWE')
    let myerc721 = new ethers.Contract(ERC721Addr.address, ERC721ABI, provider)

    let filter = myerc721.filters.Transfer()

    // let filter = myerc20.filters.Transfer(owner.address)
    // let filter = myerc20.filters.Transfer(null, owner.address)
    // logsFrom = await erc20.queryFilter(filter, -10, "latest");

    // filter = {
    //     address: ERC20Addr.address,
    //     topics: [
    //         ethers.utils.id("Transfer(address,address,uint256)")
    //     ]
    // }

    provider.on(filter, (event) => {
        console.log(event)
      //  parseTransferEvent(event);
    })
}

main()