import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Member',
    route: '/member',
    translationKey: 'global.menu.entities.member',
  },
  {
    name: 'MemberTag',
    route: '/member-tag',
    translationKey: 'global.menu.entities.memberTag',
  },
  {
    name: 'Tag',
    route: '/tag',
    translationKey: 'global.menu.entities.tag',
  },
  {
    name: 'TagType',
    route: '/tag-type',
    translationKey: 'global.menu.entities.tagType',
  },
];
