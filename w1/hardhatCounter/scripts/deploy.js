// // We require the Hardhat Runtime Environment explicitly here. This is optional
// // but useful for running the script in a standalone fashion through `node <script>`.
// //
// // You can also run a script with `npx hardhat run <script>`. If you do that, Hardhat
// // will compile your contracts, add the Hardhat Runtime Environment's members to the
// // global scope, and execute the script.
// const hre = require("hardhat");

// async function main() {
//   // const currentTimestampInSeconds = Math.round(Date.now() / 1000);
//   // const unlockTime = currentTimestampInSeconds + 60;

//   // const lockedAmount = hre.ethers.parseEther("0.001");

//   // const lock = await hre.ethers.deployContract("Lock", [unlockTime], {
//   //   value: lockedAmount,
//   // });

//   const counter = await hre.ethers.deployContract("Counter");

//   // await lock.waitForDeployment();
//   // await counter.waitForDeployment();


//   // console.log(
//   //   `Lock with ${ethers.formatEther(
//   //     lockedAmount
//   //   )}ETH and unlock timestamp ${unlockTime} deployed to ${lock.target}`
//   // );

//   console.log(
//     ` ${counter.target}`
//   );
// }

// // We recommend this pattern to be able to use async/await everywhere
// // and properly handle errors.
// main().catch((error) => {
//   console.error(error);
//   process.exitCode = 1;
// });
async function main() {
  const contractFactory = await ethers.getContractFactory("Counter");
  const contract = await contractFactory.deploy();
  await contract.deployed();

  console.log("Contract address:", contract.address);
}