import { Routes } from '@angular/router';
import {ClientComponent} from "./client/client.component";
import {CompteComponent} from "./compte/compte.component";
import {NewCustomerComponent} from "./new-customer/new-customer.component";

export const routes: Routes = [
  { path : "client", component: ClientComponent},
  { path : "compte", component: CompteComponent},
  { path : "nouveau-client", component: NewCustomerComponent},
];
