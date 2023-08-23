// SPDX-License-Identifier: MIT
pragma solidity ^0.8.9;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

contract YKERC20 is ERC20 {
     constructor() ERC20("YK", "yk") {
        _mint(msg.sender, 10e18);
    }
}