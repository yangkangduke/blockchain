async function main() {
  // 获得将要部署的合约
  const YKERC20 = await ethers.getContractFactory("YKERC20");
  const yKERC20 = await YKERC20.deploy();

  console.log("erc20 deployed to:", yKERC20);
}

main()
  .then(() => process.exit(0))
  .catch(error => {
    console.error(error);
    process.exit(1);
  });