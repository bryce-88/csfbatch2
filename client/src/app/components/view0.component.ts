import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-view0',
  templateUrl: './view0.component.html',
  styleUrls: ['./view0.component.css']
})
export class View0Component implements OnInit{

  constructor(private router: Router) {}

  ngOnInit(): void {
      
  }

  goView1() {
    this.router.navigate(['/']);
  }

}
