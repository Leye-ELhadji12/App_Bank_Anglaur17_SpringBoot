import {Component, inject} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CustomerService} from "../client/customer.service";
import {Customer} from "../model/models";

@Component({
  selector: 'app-new-customer',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.sass'
})
export class NewCustomerComponent {

  customerService$ = inject(CustomerService);
  newCustomer: FormGroup = new FormGroup<any>({
    name: new FormControl(""),
    email: new FormControl("")
  });

  addNewCustomer() {
    this.newCustomer = new FormGroup({
      name: new FormControl(null, [Validators.required]),
      email: new FormControl(null, [Validators.required, Validators.email])
    })
  }
  constructor(private fb: FormBuilder) {
    //this.addNewCustomer();
    this.newCustomer = this.fb.group({
      name: new FormControl("", [Validators.required, Validators.minLength(2)]),
      email: new FormControl("", [Validators.required, Validators.email])
    })
  }
  handleSaveCustomer(){
    let customer: Customer = this.newCustomer.value;
    this.customerService$.saveCustomer(customer).subscribe({
      next: data => {
        alert("customer has been saved")
      },error: err => {
        console.log(err);
      }
    });
  }

}
