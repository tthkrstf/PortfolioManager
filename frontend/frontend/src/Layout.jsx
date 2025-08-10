import { Outlet, Link } from "react-router-dom";
import './Style.css'
const Layout = () => {
  return (
    <div>
      <nav class="nav-container" >
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/assettable">Asset Table</Link>
          </li>
          <li>
          <Link to="/addasset">Add Asset</Link>
          </li>
        </ul>
      </nav>
       <div>
      <Outlet />
      </div>
    </div>
  )
};

export {Layout}