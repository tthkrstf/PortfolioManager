import { Outlet, Link } from "react-router-dom";

const Layout = () => {
  return (
    <div>
      <nav>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/assettable">Asset Table</Link>
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