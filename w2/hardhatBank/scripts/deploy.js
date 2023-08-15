async function main() {
  // 获得将要部署的合约
  const Greeter = await ethers.getContractFactory("Bank");
  const greeter = await Greeter.deploy();

  console.log("Greeter deployed to:", greeter.address);
}
// goerli网络合约地址：0x9925a314E8083c1D9C96878BE732Cd8ce81aAB6c
main()
  .then(() => process.exit(0))
  .catch(error => {
    console.error(error);
    process.exit(1);
  });